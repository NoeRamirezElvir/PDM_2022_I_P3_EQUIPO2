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
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterUnidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerUnidadesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.UnidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.UnidadService
import kotlinx.android.synthetic.main.activity_ver_unidades.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerUnidadesActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterUnidad.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerUnidadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerUnidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerUnidades)

        layoutManager = LinearLayoutManager(this)
        binding.contentUnidades.recyclerUnidades.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetUnidades()


        binding.fabUnidades.setOnClickListener { view ->
            val intent = Intent(this, RegistrarUnidadActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarUnidades.setOnClickListener { v ->
            callServiceGetUnidades()
        }
    }
    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarUnidad)
        val lista = listOf<String>("ID","Tipo")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarUnidad.hint = "ID"
                    binding.txtBuscarUnidad.setText("")
                    binding.txtBuscarUnidad.inputType = 2
                    binding.btnBuscarUniadad.setOnClickListener {
                        if(txtBuscarUnidad.text.isNullOrEmpty()){
                            Toast.makeText(this@VerUnidadesActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetUnidadId()
                            txtBuscarUnidad.setText(null)
                        }
                    }

                }else if(lista[p2] == "Tipo"){
                    binding.txtBuscarUnidad.hint = "Tipo"
                    binding.txtBuscarUnidad.setText("")
                    binding.txtBuscarUnidad.inputType = 1
                    binding.btnBuscarUniadad.setOnClickListener {
                        if(txtBuscarUnidad.text.isNullOrEmpty()){
                            Toast.makeText(this@VerUnidadesActivity,"Tipo esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetUnidadTipo()
                            txtBuscarUnidad.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    fun callServiceGetUnidades() {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<List<UnidadDataCollectionItem>> = unidadService.listUnidades()
        result.enqueue(
            object : Callback<List<UnidadDataCollectionItem>> {
                override fun onFailure(call: Call<List<UnidadDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerUnidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<UnidadDataCollectionItem>>,
                    response: Response<List<UnidadDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }
    private fun callServiceGetUnidadId() {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<UnidadDataCollectionItem> = unidadService.getUnidadById(txtBuscarUnidad.text.toString().toLong())
        result.enqueue(object : Callback<UnidadDataCollectionItem> {
            override fun onFailure(call: Call<UnidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerUnidadesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<UnidadDataCollectionItem>,
                response: Response<UnidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerUnidadesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerUnidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }
    private fun callServiceGetUnidadTipo() {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<UnidadDataCollectionItem> = unidadService.getUnidadByNombre(txtBuscarUnidad.text.toString())
        result.enqueue(object : Callback<UnidadDataCollectionItem> {
            override fun onFailure(call: Call<UnidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerUnidadesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<UnidadDataCollectionItem>,
                response: Response<UnidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerUnidadesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerUnidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    fun actualizarRecyclerView(pLista: List<UnidadDataCollectionItem>?) {
        adapter = RecyclerAdapterUnidad(pLista,this)
        binding.contentUnidades.recyclerUnidades.adapter = adapter
    }
    fun callServiceDeleteUnidad(listaunidad : List<UnidadDataCollectionItem>?, position: Int) {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<ResponseBody> = unidadService.deleteUnidad(listaunidad!![position].id_unidad!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerUnidadesActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerUnidadesActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerUnidadesActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerUnidadesActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaUnidad : List<UnidadDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistrarUnidadActivity::class.java)
        intent.putExtra("unidad",listaUnidad!![position])
        startActivity(intent)
    }

}