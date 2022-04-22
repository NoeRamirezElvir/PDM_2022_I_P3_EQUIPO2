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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterVacuna
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerVacunasBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.VacunaDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.VacunaService
import kotlinx.android.synthetic.main.activity_ver_vacunas.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerVacunasActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterVacuna.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerVacunasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerVacunasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVacuna)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentVacunas.recyclerVacunas.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetVacunas()
        //---------------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarVacunaActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarVacunas.setOnClickListener {v ->
            callServiceGetVacunas()
        }
    }

    private fun callServiceGetVacunaId() {
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<VacunaDataCollectionItem> = vacunaService.getVacunaById(txtBuscarIdVacuna.text.toString().toLong())
        result.enqueue(object : Callback<VacunaDataCollectionItem>{
            override fun onFailure(call: Call<VacunaDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerVacunasActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<VacunaDataCollectionItem>,
                response: Response<VacunaDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerVacunasActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerVacunasActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarVacuna)
        val lista = listOf<String>("ID","Nombre")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarIdVacuna.hint = "ID"
                    binding.txtBuscarIdVacuna.setText("")
                    binding.txtBuscarIdVacuna.inputType = 2
                    binding.imbtBuscarVacunas.setOnClickListener {
                        if(txtBuscarIdVacuna.text.isNullOrEmpty()){
                            Toast.makeText(this@VerVacunasActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetVacunaId()
                            txtBuscarIdVacuna.setText(null)
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarIdVacuna.hint = "Nombre"
                    binding.txtBuscarIdVacuna.setText("")
                    binding.txtBuscarIdVacuna.inputType = 1
                    binding.imbtBuscarVacunas.setOnClickListener {
                        if(txtBuscarIdVacuna.text.isNullOrEmpty()){
                            Toast.makeText(this@VerVacunasActivity,"Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetVacunaNombre()
                            txtBuscarIdVacuna.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun callServiceGetVacunaNombre() {
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<VacunaDataCollectionItem> = vacunaService.getVacunaByNombre(txtBuscarIdVacuna.text.toString())
        result.enqueue(object : Callback<VacunaDataCollectionItem>{
            override fun onFailure(call: Call<VacunaDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerVacunasActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<VacunaDataCollectionItem>,
                response: Response<VacunaDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerVacunasActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerVacunasActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    fun callServiceGetVacunas() {
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<List<VacunaDataCollectionItem>> = vacunaService.listVacunas()
        result.enqueue(
            object : Callback<List<VacunaDataCollectionItem>> {
                override fun onFailure(call: Call<List<VacunaDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerVacunasActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<VacunaDataCollectionItem>>,
                    response: Response<List<VacunaDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }

    fun actualizarRecyclerView(pLista: List<VacunaDataCollectionItem>?){
        adapter = RecyclerAdapterVacuna(pLista , this)
        binding.contentVacunas.recyclerVacunas.adapter = adapter
    }
    fun callServiceDeleteVacuna(listaVacuna : List<VacunaDataCollectionItem>?, position: Int) {
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<ResponseBody> = vacunaService.deleteVacuna(listaVacuna!![position].id_vacuna!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerVacunasActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerVacunasActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerVacunasActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerVacunasActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaVacuna : List<VacunaDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistrarVacunaActivity::class.java)
        intent.putExtra("vacuna",listaVacuna!![position])
        startActivity(intent)
    }

}