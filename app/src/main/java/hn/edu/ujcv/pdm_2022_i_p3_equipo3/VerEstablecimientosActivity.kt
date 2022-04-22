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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEstablecimiento
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerEstablecimientosBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EstablecimientoSaludDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EstablecimientoService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RegionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_establecimientos.*
import kotlinx.android.synthetic.main.activity_ver_region_sanitaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerEstablecimientosActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterEstablecimiento.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerEstablecimientosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerEstablecimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerEstablecimiento)

        layoutManager = LinearLayoutManager(this)
        binding.contentEstablecimientos.recyclerViewEstablecimiento.layoutManager = layoutManager
        callServiceGetEstablecimientos()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarEstablecimientoActivity::class.java)
            startActivity(intent)
        }

        spinnerBuscarEstablecimiento()

        txtBuscarEstablecimiento.doAfterTextChanged {
            if (txtBuscarEstablecimiento.text.isEmpty())
                callServiceGetEstablecimientos()
        }
    }

    fun actualizarRecyclerView(lista: List<EstablecimientoSaludDataCollectionItem>? = null) {
        adapter = RecyclerAdapterEstablecimiento(lista, this@VerEstablecimientosActivity)
        binding.contentEstablecimientos.recyclerViewEstablecimiento.adapter = adapter
    }

    fun callServiceGetEstablecimientos() {
        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<List<EstablecimientoSaludDataCollectionItem>> = establecimientoService
            .listEstablecimientos()
        result.enqueue(object: Callback<List<EstablecimientoSaludDataCollectionItem>> {
            override fun onFailure(
                call: Call<List<EstablecimientoSaludDataCollectionItem>>,
                t: Throwable
            ) {
                Toast.makeText(this@VerEstablecimientosActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<EstablecimientoSaludDataCollectionItem>>,
                response: Response<List<EstablecimientoSaludDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }

    private fun callServiceGetEstablecimiento() {
        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<EstablecimientoSaludDataCollectionItem> = establecimientoService
            .getEstablecimientosById(txtBuscarEstablecimiento.text.toString().toLong())
        result.enqueue(object: Callback<EstablecimientoSaludDataCollectionItem> {
            override fun onFailure(
                call: Call<EstablecimientoSaludDataCollectionItem>,
                t: Throwable
            ) {
                Toast.makeText(this@VerEstablecimientosActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<EstablecimientoSaludDataCollectionItem>,
                response: Response<EstablecimientoSaludDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEstablecimientosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEstablecimientosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerEstablecimientosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun callServiceDeleteEstablecimiento(
        listaEstablecimiento: List<EstablecimientoSaludDataCollectionItem>, position:Int) {

        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<ResponseBody> = establecimientoService
            .deleteEstablecimiento(listaEstablecimiento[position].id_establecimiento!!)
        result.enqueue(object: Callback<ResponseBody> {
            override fun onFailure(
                call: Call<ResponseBody>,
                t: Throwable
            ) {
                Toast.makeText(this@VerEstablecimientosActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    Toast.makeText(this@VerEstablecimientosActivity,
                        "Eliminado Exitosamente", Toast.LENGTH_LONG).show()
                    callServiceGetEstablecimientos()
                } else if (response.code()==401) {
                    Toast.makeText(this@VerEstablecimientosActivity,
                        "Sesion Expirada", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@VerEstablecimientosActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    fun enviar(context: Context, listaEstablecimiento: List<EstablecimientoSaludDataCollectionItem>,
               position: Int) {

        val intent = Intent(context, RegistrarEstablecimientoActivity::class.java)
        intent.putExtra("establecimiento", listaEstablecimiento!![position])
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun spinnerBuscarEstablecimiento(){
        val spinner = findViewById<Spinner>(R.id.spnBuscarEstablecimiento)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarEstablecimiento.hint = "ID"
                    binding.txtBuscarEstablecimiento.setText("")
                    binding.txtBuscarEstablecimiento.inputType = 2
                    binding.btnBuscarEstablecimiento.setOnClickListener {
                        if(txtBuscarEstablecimiento.text.isNullOrEmpty()){
                            Toast.makeText(this@VerEstablecimientosActivity,
                                "ID esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetEstablecimiento()
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarEstablecimiento.hint = "Nombre"
                    binding.txtBuscarEstablecimiento.setText("")
                    binding.txtBuscarEstablecimiento.inputType = 1
                    binding.btnBuscarEstablecimiento.setOnClickListener {
                        if(txtBuscarEstablecimiento.text.isNullOrEmpty()){
                            Toast.makeText(this@VerEstablecimientosActivity,
                                "Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetNombreEstablecimiento()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetNombreEstablecimiento() {
        val establecimientoService: EstablecimientoService = RestEngine
            .buildService().create(EstablecimientoService::class.java)
        val result: Call<EstablecimientoSaludDataCollectionItem> = establecimientoService
            .getEstablecimientosByNombre(txtBuscarEstablecimiento.text.toString())
        result.enqueue(object: Callback<EstablecimientoSaludDataCollectionItem>{
            override fun onFailure(call: Call<EstablecimientoSaludDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerEstablecimientosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<EstablecimientoSaludDataCollectionItem>,
                response: Response<EstablecimientoSaludDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEstablecimientosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEstablecimientosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerEstablecimientosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

