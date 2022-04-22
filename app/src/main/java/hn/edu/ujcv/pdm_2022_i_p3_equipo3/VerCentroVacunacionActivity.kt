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
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCentroVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCentroVacunacionBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CentroVacunacionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.MunicipioService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_centro_vacunacion.*
import kotlinx.android.synthetic.main.activity_ver_region_sanitaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCentroVacunacionActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCentroVacunacion.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCentroVacunacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerCentroVacunacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerCentro)

        layoutManager = LinearLayoutManager(this)
        binding.contentCentro.recyclerViewCentro.layoutManager = layoutManager
        callServiceGetCentros()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarCentroVacActivity::class.java)
            startActivity(intent)
        }

        spinnerBuscarCentro()

        txtBuscarCentro.doAfterTextChanged {
            if (txtBuscarCentro.text.isEmpty())
                callServiceGetCentros()
        }
    }

    fun actualizarRecyclerView(lista: List<CentroVacunacionDataCollectionItem>? = null) {
        adapter = RecyclerAdapterCentroVacunacion(lista, this@VerCentroVacunacionActivity)
        binding.contentCentro.recyclerViewCentro.adapter = adapter
    }

    fun callServiceGetCentros() {
        val centroService: CentroVacunacionService = RestEngine.buildService()
            .create(CentroVacunacionService::class.java)
        val result: Call<List<CentroVacunacionDataCollectionItem>> = centroService
            .listCentrosVacunacion()
        result.enqueue(object: Callback<List<CentroVacunacionDataCollectionItem>> {
            override fun onFailure(
                call: Call<List<CentroVacunacionDataCollectionItem>>,
                t: Throwable
            ) {
                Toast.makeText(this@VerCentroVacunacionActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<CentroVacunacionDataCollectionItem>>,
                response: Response<List<CentroVacunacionDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }

    private fun callServiceGetCentro() {
        val centroService: CentroVacunacionService = RestEngine.buildService()
            .create(CentroVacunacionService::class.java)
        val result: Call<CentroVacunacionDataCollectionItem> = centroService
            .getCentrosVacunacionById(txtBuscarCentro.text.toString().toLong())
        result.enqueue(object: Callback<CentroVacunacionDataCollectionItem> {
            override fun onFailure(
                call: Call<CentroVacunacionDataCollectionItem>,
                t: Throwable
            ) {
                Toast.makeText(this@VerCentroVacunacionActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CentroVacunacionDataCollectionItem>,
                response: Response<CentroVacunacionDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCentroVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCentroVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCentroVacunacionActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun callServiceDeleteCentro(lista: List<CentroVacunacionDataCollectionItem>, position:Int) {
        val centroService: CentroVacunacionService = RestEngine.buildService()
            .create(CentroVacunacionService::class.java)
        val result: Call<ResponseBody> = centroService
            .deleteCentro(lista[position].id_centroVacunacion!!)
        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(
                call: Call<ResponseBody>,
                t: Throwable
            ) {
                Toast.makeText(this@VerCentroVacunacionActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VerCentroVacunacionActivity,
                        "Eliminado Exitosamente", Toast.LENGTH_LONG).show()
                    callServiceGetCentros()
                } else if (response.code()==401) {
                    Toast.makeText(this@VerCentroVacunacionActivity,
                        "Sesion Expirada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@VerCentroVacunacionActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun enviar(context:Context, lista: List<CentroVacunacionDataCollectionItem>?, position:Int) {
        val intent = Intent(context, RegistrarCentroVacActivity::class.java)
        intent.putExtra("centro", lista!![position])
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun spinnerBuscarCentro(){
        val spinner = findViewById<Spinner>(R.id.spnBuscarCentro)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarCentro.hint = "ID"
                    binding.txtBuscarCentro.setText("")
                    binding.txtBuscarCentro.inputType = 2
                    binding.btnBuscarCentro.setOnClickListener {
                        if(txtBuscarCentro.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCentroVacunacionActivity,
                                "ID esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetCentro()
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarCentro.hint = "Nombre"
                    binding.txtBuscarCentro.setText("")
                    binding.txtBuscarCentro.inputType = 1
                    binding.btnBuscarCentro.setOnClickListener {
                        if(txtBuscarCentro.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCentroVacunacionActivity,
                                "Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetNombreCentro()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetNombreCentro() {
        val centroService: CentroVacunacionService = RestEngine.buildService().create(CentroVacunacionService::class.java)
        val result: Call<CentroVacunacionDataCollectionItem> = centroService
            .getCentrosVacunacionByNombre(txtBuscarCentro.text.toString())
        result.enqueue(object: Callback<CentroVacunacionDataCollectionItem>{
            override fun onFailure(call: Call<CentroVacunacionDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCentroVacunacionActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CentroVacunacionDataCollectionItem>,
                response: Response<CentroVacunacionDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCentroVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCentroVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCentroVacunacionActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

