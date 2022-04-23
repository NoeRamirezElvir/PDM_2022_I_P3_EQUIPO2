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
import androidx.core.widget.doAfterTextChanged
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnet
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetVacunacionBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CargosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CarnetEncabezadoService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCarnetVacunacionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerCarnetVacunacionBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnet.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCarnetVacunacionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCarnet)
        //-------------------------------------------------
        callServiceGetCarnetEncabezado()
        binding.fab2.setOnClickListener { callServiceGetCarnetEncabezado()}
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnet.recyclerCarnet.layoutManager = layoutManager
        //-----------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarCarnetActivity::class.java)
            startActivity(intent)
        }
        spinnerBusqueda()
        binding.txtBuscarNumeroCarnet.doAfterTextChanged {
            if(binding.txtBuscarNumeroCarnet.text.isNullOrEmpty()){
                callServiceGetCarnetEncabezado()
            }
        }
    }
    fun callServiceGetCarnetEncabezado() {
        val carnetService: CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<List<CarnetEncabezadoDataCollectionItem>> = carnetService.listCarnetEncabezado()
        result.enqueue(object: Callback<List<CarnetEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerCarnetVacunacionActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetEncabezadoDataCollectionItem>>,
                response: Response<List<CarnetEncabezadoDataCollectionItem>>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(response.body()!!)
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCarnetVacunacionActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun callServiceGetCarnetEncabezadoID() {
        val carnetService:CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<CarnetEncabezadoDataCollectionItem> = carnetService.getCarnetEncabezadoById(binding.txtBuscarNumeroCarnet.text.toString().toLong())
        result.enqueue(object: Callback<CarnetEncabezadoDataCollectionItem>{
            override fun onFailure(call: Call<CarnetEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCarnetVacunacionActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CarnetEncabezadoDataCollectionItem>,
                response: Response<CarnetEncabezadoDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCarnetVacunacionActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun callServiceGetCarnetEncabezadoNumero() {
        val carnetService:CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<CarnetEncabezadoDataCollectionItem> = carnetService.getCarnetEncabezadoByNumeroCarnet(binding.txtBuscarNumeroCarnet.text.toString().toLong())
        result.enqueue(object: Callback<CarnetEncabezadoDataCollectionItem>{
            override fun onFailure(call: Call<CarnetEncabezadoDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCarnetVacunacionActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CarnetEncabezadoDataCollectionItem>,
                response: Response<CarnetEncabezadoDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCarnetVacunacionActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCarnetVacunacionActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    fun callServiceDeleteCarnetEncabezado(listaCarnet : List<CarnetEncabezadoDataCollectionItem>?, position: Int) {
        val carnetService: CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<ResponseBody> = carnetService.deleteCarnetEncabezado(listaCarnet!![position].id_carnetEncabezado!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCarnetVacunacionActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(this@VerCarnetVacunacionActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@VerCarnetVacunacionActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@VerCarnetVacunacionActivity,"Fallo al traer el item o se esta utilizando en otro registro",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    fun enviar(context: Context, listaCarnet: List<CarnetEncabezadoDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistrarCarnetActivity::class.java)
        intent.putExtra("encabezado",listaCarnet!![position])
        startActivity(intent)
        this.finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    fun actualizarRecyclerView(lista:List<CarnetEncabezadoDataCollectionItem>){
        adapter = RecyclerAdapterCarnet(lista,this@VerCarnetVacunacionActivity)
        binding.contentCarnet.recyclerCarnet.adapter = adapter
    }
    fun abrirCarnetDetalles(context: Context, listaCarnet: List<CarnetEncabezadoDataCollectionItem>?, position :Int){
        val intent = Intent(context,VerCarnetMasDetalleActivity::class.java)
        intent.putExtra("encabezado",listaCarnet!![position])
        startActivity(intent)
    }
    private fun spinnerBusqueda() {
        val spinner = findViewById<Spinner>(R.id.spnBuscarIDNumeroCarnetE)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNumero)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner, lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (lista[p2] == "ID") {
                    binding.txtBuscarNumeroCarnet.hint = "ID"
                    binding.txtBuscarNumeroCarnet.setText("")
                    binding.txtBuscarNumeroCarnet.inputType = 2
                    binding.imbtBuscarCarnet.setOnClickListener {
                        if (binding.txtBuscarNumeroCarnet.text.isNullOrEmpty()) {
                            Toast.makeText(
                                this@VerCarnetVacunacionActivity,
                                "ID esta Vacío",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            callServiceGetCarnetEncabezadoID()
                        }
                    }

                } else if (lista[p2] == "#Carnet") {
                    binding.txtBuscarNumeroCarnet.hint = "No. Carnet"
                    binding.txtBuscarNumeroCarnet.setText("")
                    binding.txtBuscarNumeroCarnet.inputType = 2
                    binding.imbtBuscarCarnet.setOnClickListener {
                        if (binding.txtBuscarNumeroCarnet.text.isNullOrEmpty()) {
                            Toast.makeText(
                                this@VerCarnetVacunacionActivity,
                                "El numero esta Vacío",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            callServiceGetCarnetEncabezadoNumero()
                        }
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}