package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterPaises
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerPaisesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.PaisesDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CentroVacunacionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.PaisesService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_centro_vacunacion.*
import kotlinx.android.synthetic.main.activity_ver_paises.*
import kotlinx.android.synthetic.main.activity_ver_region_sanitaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class VerPaisesActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterPaises.ViewHolder>? = null
    private lateinit var binding:ActivityVerPaisesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerPaisesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerPaises)

        layoutManager = LinearLayoutManager(this)
        binding.contentPaises.recyclerPaises.layoutManager = layoutManager
        callServiceGetPaises()

        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarPaisActivity::class.java)
            startActivity(intent)
        }

        spinnerBuscarPais()

        txtBuscarPais.doAfterTextChanged {
            if (txtBuscarPais.text.isEmpty())
                callServiceGetPaises()
        }
    }

    fun actualizarRecyclerView(listaPaises: List<PaisesDataCollectionItem>? = null){
        adapter = RecyclerAdapterPaises(listaPaises, this@VerPaisesActivity)
        binding.contentPaises.recyclerPaises.adapter = adapter
    }

    fun callServiceGetPaises() {
        val paisesService: PaisesService = RestEngine.buildService()
            .create(PaisesService::class.java)
        val result: Call<List<PaisesDataCollectionItem>> = paisesService.listPaises()
        result.enqueue(object : Callback<List<PaisesDataCollectionItem>> {
            override fun onFailure(call: Call<List<PaisesDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerPaisesActivity, "Error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<PaisesDataCollectionItem>>,
                response: Response<List<PaisesDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }

    private fun callServiceGetPais() {
        val paisesService: PaisesService = RestEngine.buildService()
            .create(PaisesService::class.java)
        val result: Call<PaisesDataCollectionItem> = paisesService
            .getPaisesById(txtBuscarPais.text.toString().toLong())
        result.enqueue(object : Callback<PaisesDataCollectionItem>{
            override fun onFailure(call: Call<PaisesDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerPaisesActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<PaisesDataCollectionItem>,
                response: Response<PaisesDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerPaisesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerPaisesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerPaisesActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun callServiceDeletePais(listaPaises: List<PaisesDataCollectionItem>, position:Int) {
        val paisesService: PaisesService = RestEngine.buildService()
            .create(PaisesService::class.java)
        val result: Call<ResponseBody> = paisesService.deletePais(listaPaises[position].id_pais!!)
        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@VerPaisesActivity, "Error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VerPaisesActivity, "Eliminado exitosamente",
                        Toast.LENGTH_LONG).show()
                    callServiceGetPaises()
                } else if (response.code()==401) {
                    Toast.makeText(this@VerPaisesActivity,
                        "Sesion Expirada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@VerPaisesActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun enviar(context: Context, listaPaises: List<PaisesDataCollectionItem>?, position:Int) {
        val intent = Intent(context, RegistrarPaisActivity::class.java)
        intent.putExtra("paises", listaPaises!![position])
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun spinnerBuscarPais(){
        val spinner = findViewById<Spinner>(R.id.spnBuscarPais)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarPais.hint = "ID"
                    binding.txtBuscarPais.setText("")
                    binding.txtBuscarPais.inputType = 2
                    binding.btnBuscarPais.setOnClickListener {
                        if(txtBuscarPais.text.isNullOrEmpty()){
                            Toast.makeText(this@VerPaisesActivity,
                                "ID esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetPais()
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarPais.hint = "Nombre"
                    binding.txtBuscarPais.setText("")
                    binding.txtBuscarPais.inputType = 1
                    binding.btnBuscarPais.setOnClickListener {
                        if(txtBuscarPais.text.isNullOrEmpty()){
                            Toast.makeText(this@VerPaisesActivity,
                                "Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetNombrePais()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetNombrePais() {
        val paisService: PaisesService = RestEngine.buildService().create(
            PaisesService::class.java)
        val result: Call<PaisesDataCollectionItem> = paisService
            .getPaisesByNombre(txtBuscarPais.text.toString())
        result.enqueue(object: Callback<PaisesDataCollectionItem>{
            override fun onFailure(call: Call<PaisesDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerPaisesActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<PaisesDataCollectionItem>,
                response: Response<PaisesDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerPaisesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerPaisesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerPaisesActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

