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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCivilesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_civiles.*
import kotlinx.android.synthetic.main.activity_ver_vacunas.*
import kotlinx.android.synthetic.main.card_layout_civil.*
import kotlinx.android.synthetic.main.content_registar_civil_comorbilidad.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCivilesActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCivil.ViewHolder>? = null
    private lateinit var binding: ActivityVerCivilesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCivilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCiviles)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentCiviles.recyclerCiviles.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetCiviles()
        //-----------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarCivilActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarCiviles.setOnClickListener { v ->
            callServiceGetCiviles()
        }
    }
    fun actualizarRecyclerView(pLista: List<CivilDataCollectionItem>?){
        adapter = RecyclerAdapterCivil(pLista, this)
        binding.contentCiviles.recyclerCiviles.adapter = adapter
    }
    fun callServiceGetCiviles() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<List<CivilDataCollectionItem>> = civilService.listCiviles()
        result.enqueue(
            object : Callback<List<CivilDataCollectionItem>> {
                override fun onFailure(call: Call<List<CivilDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerCivilesActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CivilDataCollectionItem>>,
                    response: Response<List<CivilDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }
    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarCivil)
        val lista = listOf<String>("ID","Nombre")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarIdCiviles.hint = "ID"
                    binding.txtBuscarIdCiviles.setText("")
                    binding.txtBuscarIdCiviles.inputType = 2
                    binding.imbtnBuscarCivil.setOnClickListener {
                        if(txtBuscarIdCiviles.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCivilesActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetCivilId()
                            txtBuscarIdCiviles.setText(null)
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarIdCiviles.hint = "Nombre"
                    binding.txtBuscarIdCiviles.setText("")
                    binding.txtBuscarIdCiviles.inputType = 1
                    binding.imbtnBuscarCivil.setOnClickListener {
                        if(txtBuscarIdCiviles.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCivilesActivity,"Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetCivilNombre()
                            txtBuscarIdCiviles.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun callServiceGetCivilNombre() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<CivilDataCollectionItem> = civilService.getCivilByNombre(txtBuscarIdCiviles.text.toString())
        result.enqueue(object : Callback<CivilDataCollectionItem>{
            override fun onFailure(call: Call<CivilDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerCivilesActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CivilDataCollectionItem>,
                response: Response<CivilDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerCivilesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerCivilesActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    private fun callServiceGetCivilId() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<CivilDataCollectionItem> = civilService.getCivilById(txtBuscarIdCiviles.text.toString().toLong())
        result.enqueue(object : Callback<CivilDataCollectionItem>{
                override fun onFailure(call: Call<CivilDataCollectionItem>, t: Throwable) {

                    Toast.makeText(this@VerCivilesActivity,"Error",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CivilDataCollectionItem>,
                    response: Response<CivilDataCollectionItem>
                ) {
                    if (response.isSuccessful){
                        actualizarRecyclerView(listOf(response.body()!!))
                    }else if (response.code() == 404){
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@VerCivilesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerCivilesActivity,"Error", Toast.LENGTH_LONG).show()
                    }

                    //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
                }
            }
        )
    }
    fun callServiceDeleteCivil(listaCivil : List<CivilDataCollectionItem>?, position: Int) {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<ResponseBody> = civilService.deleteCivil(listaCivil!![position].id_civil!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCivilesActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerCivilesActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerCivilesActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerCivilesActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context,listaCivil : List<CivilDataCollectionItem>? ,position :Int){
        val intent = Intent(context,RegistrarCivilActivity::class.java)
        intent.putExtra("civil",listaCivil!![position])
        startActivity(intent)
    }

}
