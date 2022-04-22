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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterFabricante
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerFabricantesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.FabricanteDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.FabricanteService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_fabricantes.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerFabricantesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerFabricantesBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterFabricante.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerFabricantesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerFabricantes)

        layoutManager = LinearLayoutManager(this)
        binding.contentFabricantes.recyclerViewFabricantes.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetFabs()

        binding.fabFabricantes.setOnClickListener { view ->
            val intent = Intent(this, RegistarFabricanteActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarFabricantes.setOnClickListener { v ->
            callServiceGetFabs()
        }

    }
    fun actualizarRecyclerView(pLista: List<FabricanteDataCollectionItem>?) {
        adapter = RecyclerAdapterFabricante(pLista,this)
        binding.contentFabricantes.recyclerViewFabricantes.adapter = adapter
    }
    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarFabricante)
        val lista = listOf<String>("ID","Nombre")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarFabricante.hint = "ID"
                    binding.txtBuscarFabricante.setText("")
                    binding.txtBuscarFabricante.inputType = 2
                    binding.btnBuscarFabricante.setOnClickListener {
                        if(txtBuscarFabricante.text.isNullOrEmpty()){
                            Toast.makeText(this@VerFabricantesActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetFabId()
                            txtBuscarFabricante.setText(null)
                        }
                    }
                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarFabricante.hint = "Nombre"
                    binding.txtBuscarFabricante.setText("")
                    binding.txtBuscarFabricante.inputType  = 1
                    binding.btnBuscarFabricante.setOnClickListener {
                        if(txtBuscarFabricante.text.isNullOrEmpty()){
                            Toast.makeText(this@VerFabricantesActivity,"Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetFabricanteNombre()
                            txtBuscarFabricante.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun callServiceGetFabricanteNombre() {
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<FabricanteDataCollectionItem> = fabService.getFabByNombre(txtBuscarFabricante.text.toString())
        result.enqueue(object : Callback<FabricanteDataCollectionItem>{
            override fun onFailure(call: Call<FabricanteDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerFabricantesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<FabricanteDataCollectionItem>,
                response: Response<FabricanteDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerFabricantesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerFabricantesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    private fun callServiceGetFabId() {
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<FabricanteDataCollectionItem> = fabService.getFabById(txtBuscarFabricante.text.toString().toLong())
        result.enqueue(object : Callback<FabricanteDataCollectionItem>{
            override fun onFailure(call: Call<FabricanteDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerFabricantesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<FabricanteDataCollectionItem>,
                response: Response<FabricanteDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerFabricantesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerFabricantesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    fun callServiceGetFabs() {
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<List<FabricanteDataCollectionItem>> = fabService.listFabs()
        result.enqueue(
            object : Callback<List<FabricanteDataCollectionItem>> {
                override fun onFailure(call: Call<List<FabricanteDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerFabricantesActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<FabricanteDataCollectionItem>>,
                    response: Response<List<FabricanteDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }
    fun callServiceDeleteFab(listaFab : List<FabricanteDataCollectionItem>?, position: Int) {
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<ResponseBody> = fabService.deleteFab(listaFab!![position].id_fabricante!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerFabricantesActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerFabricantesActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerFabricantesActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerFabricantesActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaFab : List<FabricanteDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistarFabricanteActivity::class.java)
        intent.putExtra("fab",listaFab!![position])
        startActivity(intent)
    }

}