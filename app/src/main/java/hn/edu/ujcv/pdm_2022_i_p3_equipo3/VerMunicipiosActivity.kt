package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterMunicipio
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerMunicipiosBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.MunicipioService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RegionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_municipios.*
import kotlinx.android.synthetic.main.activity_ver_region_sanitaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerMunicipiosActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterMunicipio.ViewHolder>? = null
    private lateinit var binding: ActivityVerMunicipiosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerMunicipiosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerMunicipio)

        layoutManager = LinearLayoutManager(this)
        binding.contentMunicipio.recyclerMunicipios.layoutManager = layoutManager
        callServiceGetMunicipios()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarMunicipioActivity::class.java)
            startActivity(intent)
        }

        spinnerBuscarMunicipio()

        txtBuscarMunicipio.doAfterTextChanged {
            if (txtBuscarMunicipio.text.isEmpty())
                callServiceGetMunicipios()
        }
    }

    fun actualizarRecyclerView(lista: List<MunicipioDataCollectionItem>? = null) {
        adapter = RecyclerAdapterMunicipio(lista, this@VerMunicipiosActivity)
        binding.contentMunicipio.recyclerMunicipios.adapter = adapter
    }

    fun callServiceGetMunicipios() {
        val municipioService: MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<List<MunicipioDataCollectionItem>> = municipioService.listMunicipios()
        result.enqueue(object : Callback<List<MunicipioDataCollectionItem>> {
            override fun onFailure(call: Call<List<MunicipioDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerMunicipiosActivity, "Error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<MunicipioDataCollectionItem>>,
                response: Response<List<MunicipioDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }

    private fun callServiceGetMunicipio() {
        val municipioService: MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<MunicipioDataCollectionItem> = municipioService
            .getMunicipiosById(txtBuscarMunicipio.text.toString().toLong())
        result.enqueue(object : Callback<MunicipioDataCollectionItem>{
            override fun onFailure(call: Call<MunicipioDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerMunicipiosActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<MunicipioDataCollectionItem>,
                response: Response<MunicipioDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerMunicipiosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerMunicipiosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerMunicipiosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun callServiceDeleteMunicipio(listaMunicipio: List<MunicipioDataCollectionItem>?,
                                   position:Int) {
        val municipioService: MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<ResponseBody> = municipioService.deleteMunicipio(listaMunicipio!![position]
            .id_municipio!!)
        result.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@VerMunicipiosActivity, "Error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VerMunicipiosActivity, "Eliminado exitosamente",
                        Toast.LENGTH_LONG).show()
                    callServiceGetMunicipios()
                } else if (response.code()==401) {
                    Toast.makeText(this@VerMunicipiosActivity,
                        "Sesion Expirada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@VerMunicipiosActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun enviar(context: Context, listaMunicipio: List<MunicipioDataCollectionItem>?,
               position: Int) {
        val intent = Intent(context, RegistrarMunicipioActivity::class.java)
        intent.putExtra("municipio", listaMunicipio!![position])
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun spinnerBuscarMunicipio(){
        val spinner = findViewById<Spinner>(R.id.spnBuscarMunicipio)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarMunicipio.hint = "ID"
                    binding.txtBuscarMunicipio.setText("")
                    binding.txtBuscarMunicipio.inputType = 2
                    binding.btnBuscarMunicipio.setOnClickListener {
                        if(txtBuscarMunicipio.text.isNullOrEmpty()){
                            Toast.makeText(this@VerMunicipiosActivity,
                                "ID esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetMunicipio()
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarMunicipio.hint = "Nombre"
                    binding.txtBuscarMunicipio.setText("")
                    binding.txtBuscarMunicipio.inputType = 1
                    binding.btnBuscarMunicipio.setOnClickListener {
                        if(txtBuscarMunicipio.text.isNullOrEmpty()){
                            Toast.makeText(this@VerMunicipiosActivity,
                                "Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetNombreMunicipio()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetNombreMunicipio() {
        val municipioService: MunicipioService = RestEngine.buildService().create(MunicipioService::class.java)
        val result: Call<MunicipioDataCollectionItem> = municipioService
            .getMunicipioByNombre(txtBuscarMunicipio.text.toString())
        result.enqueue(object: Callback<MunicipioDataCollectionItem>{
            override fun onFailure(call: Call<MunicipioDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerMunicipiosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<MunicipioDataCollectionItem>,
                response: Response<MunicipioDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerMunicipiosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerMunicipiosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerMunicipiosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

