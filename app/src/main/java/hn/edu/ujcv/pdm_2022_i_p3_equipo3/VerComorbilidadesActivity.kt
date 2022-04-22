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
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterRegion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerComorbilidadesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerRegionSanitariaBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.ComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_civiles.*
import kotlinx.android.synthetic.main.activity_ver_comorbilidades.*
import kotlinx.android.synthetic.main.activity_ver_vacunas.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerComorbilidadesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerComorbilidadesBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterComorbilidad.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerComorbilidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerComorbilidades)

        layoutManager = LinearLayoutManager(this)
        binding.contentComorbilidades.recyclerViewComorbilidad.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetComorbs()

        binding.fabComorbilidad.setOnClickListener { view ->
            val intent = Intent(this, RegistrarComorbilidad::class.java)
            startActivity(intent)
        }
        binding.fabListarComorb.setOnClickListener { v ->
            callServiceGetComorbs()
        }

    }
    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarComorb)
        val lista = listOf<String>("ID","Nombre")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarComorbilidad.hint = "ID"
                    binding.txtBuscarComorbilidad.setText("")
                    binding.txtBuscarComorbilidad.inputType = 2
                    binding.btnBuscarComorbilidad.setOnClickListener {
                        if(txtBuscarComorbilidad.text.isNullOrEmpty()){
                            Toast.makeText(this@VerComorbilidadesActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetComorbId()
                            txtBuscarComorbilidad.setText(null)
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarComorbilidad.hint = "Nombre"
                    binding.txtBuscarComorbilidad.setText("")
                    binding.txtBuscarComorbilidad.inputType = 1
                    binding.btnBuscarComorbilidad.setOnClickListener {
                        if(txtBuscarComorbilidad.text.isNullOrEmpty()){
                            Toast.makeText(this@VerComorbilidadesActivity,"Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetComorbNombre()
                            txtBuscarComorbilidad.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun callServiceGetComorbId() {
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<ComorbilidadDataCollectionItem> = comorbService.getComorbById(txtBuscarComorbilidad.text.toString().toLong())
        result.enqueue(object : Callback<ComorbilidadDataCollectionItem>{
            override fun onFailure(call: Call<ComorbilidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerComorbilidadesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<ComorbilidadDataCollectionItem>,
                response: Response<ComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerComorbilidadesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerComorbilidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }
    private fun callServiceGetComorbNombre() {
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<ComorbilidadDataCollectionItem> = comorbService.getComorbByNombre(txtBuscarComorbilidad.text.toString())
        result.enqueue(object : Callback<ComorbilidadDataCollectionItem>{
            override fun onFailure(call: Call<ComorbilidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerComorbilidadesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<ComorbilidadDataCollectionItem>,
                response: Response<ComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerComorbilidadesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerComorbilidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

     fun callServiceGetComorbs() {
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<List<ComorbilidadDataCollectionItem>> = comorbService.listComorbs()
        result.enqueue(
            object : Callback<List<ComorbilidadDataCollectionItem>> {
                override fun onFailure(call: Call<List<ComorbilidadDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerComorbilidadesActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<ComorbilidadDataCollectionItem>>,
                    response: Response<List<ComorbilidadDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }

    fun actualizarRecyclerView(pLista: List<ComorbilidadDataCollectionItem>?) {
        adapter = RecyclerAdapterComorbilidad(pLista, this)
        binding.contentComorbilidades.recyclerViewComorbilidad.adapter = adapter
    }

    fun callServiceDeleteComorb(listaComorb : List<ComorbilidadDataCollectionItem>?, position: Int) {
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<ResponseBody> = comorbService.deleteComorb(listaComorb!![position].id_comorbilidad!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerComorbilidadesActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerComorbilidadesActivity,"Eliminado exítosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerComorbilidadesActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerComorbilidadesActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaComorb : List<ComorbilidadDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistrarComorbilidad::class.java)
        intent.putExtra("comorb",listaComorb!![position])
        startActivity(intent)
    }

}