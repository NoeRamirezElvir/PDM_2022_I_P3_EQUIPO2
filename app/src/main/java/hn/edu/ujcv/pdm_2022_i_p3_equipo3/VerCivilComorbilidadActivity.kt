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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivilComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCivilComorbilidadBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_civil_comorbilidad.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCivilComorbilidadActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCivilComorbilidadBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCivilComorbilidad.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerCivilComorbilidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerCivilComorb)

        layoutManager = LinearLayoutManager(this)
        binding.contentCivilComorb.recyclerViewCivilComorbilidad.layoutManager = layoutManager
        spinnerBusqueda()
        callServiceGetCivilesComorbs()

        binding.fabCivComorb.setOnClickListener { view ->
            val intent = Intent(this, RegistarCivilComorbilidadActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarCivComorb.setOnClickListener { v ->
            callServiceGetCivilesComorbs()
        }
    }

    private fun callServiceGetCivilComorbId() {
        val civilComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<CivilComorbilidadDataCollectionItem> = civilComorbService.getCivComorbById(txtBuscarCivilComorb.text.toString().toLong())
        result.enqueue(object : Callback<CivilComorbilidadDataCollectionItem>{
            override fun onFailure(call: Call<CivilComorbilidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerCivilComorbilidadActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CivilComorbilidadDataCollectionItem>,
                response: Response<CivilComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerCivilComorbilidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerCivilComorbilidadActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    fun callServiceGetCivilesComorbs() {
        val civilComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<List<CivilComorbilidadDataCollectionItem>> = civilComorbService.listCivsComorbs()
        result.enqueue(
            object : Callback<List<CivilComorbilidadDataCollectionItem>> {
                override fun onFailure(call: Call<List<CivilComorbilidadDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerCivilComorbilidadActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CivilComorbilidadDataCollectionItem>>,
                    response: Response<List<CivilComorbilidadDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }

    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarCivComorb)
        val lista = listOf<String>("ID","Estado")
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarCivilComorb.hint = "ID"
                    binding.txtBuscarCivilComorb.setText("")
                    binding.txtBuscarCivilComorb.inputType = 2
                    binding.btnBuscarCivilComorb.setOnClickListener {
                        if(txtBuscarCivilComorb.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCivilComorbilidadActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetCivilComorbId()
                            txtBuscarCivilComorb.setText(null)
                        }
                    }

                }else if(lista[p2] == "Estado"){
                    binding.txtBuscarCivilComorb.hint = "Estado"
                    binding.txtBuscarCivilComorb.setText("")
                    binding.txtBuscarCivilComorb.inputType = 1
                    binding.btnBuscarCivilComorb.setOnClickListener {
                        if(txtBuscarCivilComorb.text.isNullOrEmpty()){
                            Toast.makeText(this@VerCivilComorbilidadActivity,"Estado esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetCivilComorbEstado()
                            txtBuscarCivilComorb.setText(null)
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }

    private fun callServiceGetCivilComorbEstado() {
        val civilComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<CivilComorbilidadDataCollectionItem> = civilComorbService.getCivComorbByEstado(txtBuscarCivilComorb.text.toString())
        result.enqueue(object : Callback<CivilComorbilidadDataCollectionItem>{
            override fun onFailure(call: Call<CivilComorbilidadDataCollectionItem>, t: Throwable) {

                Toast.makeText(this@VerCivilComorbilidadActivity,"Error",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CivilComorbilidadDataCollectionItem>,
                response: Response<CivilComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful){
                    actualizarRecyclerView(listOf(response.body()!!))
                }else if (response.code() == 404){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@VerCivilComorbilidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }else{
                    Toast.makeText(this@VerCivilComorbilidadActivity,"Error", Toast.LENGTH_LONG).show()
                }

                //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
            }
        }
        )
    }

    fun actualizarRecyclerView(pLista: List<CivilComorbilidadDataCollectionItem>?) {
        adapter = RecyclerAdapterCivilComorbilidad(pLista,this)
        binding.contentCivilComorb.recyclerViewCivilComorbilidad.adapter = adapter
    }
    fun callServiceDeleteCivilComorb(listaCivilComorb : List<CivilComorbilidadDataCollectionItem>?, position: Int) {
        val civilComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<ResponseBody> = civilComorbService.deleteCivComorb(listaCivilComorb!![position].id_civilComorbilidad!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCivilComorbilidadActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerCivilComorbilidadActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerCivilComorbilidadActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerCivilComorbilidadActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaCivilComorb : List<CivilComorbilidadDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistarCivilComorbilidadActivity::class.java)
        intent.putExtra("civilcomorb",listaCivilComorb!![position])
        startActivity(intent)
    }

}