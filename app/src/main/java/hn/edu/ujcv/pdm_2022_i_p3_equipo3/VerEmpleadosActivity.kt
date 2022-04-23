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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEmpleado
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerEmpleadoBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EmpleadosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_empleado.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerEmpleadosActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterEmpleado.ViewHolder>? = null
    private lateinit var binding: ActivityVerEmpleadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarEmpleado)
        callServiceGetEmpleados()
        binding.fab2.setOnClickListener{
            callServiceGetEmpleados()
        }
        //-----------------------------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentEmpleado.recyclerEmpleado.layoutManager = layoutManager
        //fab
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarEmpleadosActivity::class.java)
            startActivity(intent)
        }
        //Boton Buscar
        spinnerBusqueda()
        //Actualizar lista luego de borar busqueda
        binding.txtBuscarNombreEmpleado.doAfterTextChanged {
            if(txtBuscarNombreEmpleado.text.isEmpty()) {
                callServiceGetEmpleados()
            }
        }
    }

    fun actualizarRecyclerView(lista:List<EmpleadosDataCollectionItem>? = null){
        adapter = RecyclerAdapterEmpleado(lista,this@VerEmpleadosActivity)
        binding.contentEmpleado.recyclerEmpleado.adapter = adapter
    }
    fun callServiceGetEmpleados() {
        val empleadoService: EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<List<EmpleadosDataCollectionItem>> = empleadoService.listEmpleados()
        result.enqueue(object: Callback<List<EmpleadosDataCollectionItem>> {
            override fun onFailure(call: Call<List<EmpleadosDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerEmpleadosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<EmpleadosDataCollectionItem>>,
                response: Response<List<EmpleadosDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }
    private fun spinnerBusqueda(){
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarEmpleado)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarNombreEmpleado.hint = "ID"
                    binding.txtBuscarNombreEmpleado.setText("")
                    binding.txtBuscarNombreEmpleado.inputType = 2
                    binding.imbtBuscarEmpleado.setOnClickListener {
                        if(txtBuscarNombreEmpleado.text.isNullOrEmpty()){
                            Toast.makeText(this@VerEmpleadosActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetEmpleadoID()
                        }
                    }

                }else if(lista[p2] == "Nombre"){
                    binding.txtBuscarNombreEmpleado.hint = "Nombre"
                    binding.txtBuscarNombreEmpleado.setText("")
                    binding.txtBuscarNombreEmpleado.inputType = 1
                    binding.imbtBuscarEmpleado.setOnClickListener {
                        if(txtBuscarNombreEmpleado.text.isNullOrEmpty()){
                            Toast.makeText(this@VerEmpleadosActivity,"Nombre esta Vacío", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetEmpleadoNombre()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetEmpleadoNombre() {
        val empleadoService:EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadoService.getEmpleadoByNombre(txtBuscarNombreEmpleado.text.toString())
        result.enqueue(object: Callback<EmpleadosDataCollectionItem>{
            override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerEmpleadosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<EmpleadosDataCollectionItem>,
                response: Response<EmpleadosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerEmpleadosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun callServiceGetEmpleadoID() {
        val empleadoService:EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadoService.getEmpleadoById(txtBuscarNombreEmpleado.text.toString().toInt())
        result.enqueue(object: Callback<EmpleadosDataCollectionItem>{
            override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerEmpleadosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<EmpleadosDataCollectionItem>,
                response: Response<EmpleadosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerEmpleadosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    //Eliminar
    fun callServiceDeleteEmpleado(listaEmpleados : List<EmpleadosDataCollectionItem>?, position: Int) {
        val civilService: EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<ResponseBody> = civilService.deleteEmpleado(listaEmpleados!![position].id_empleado!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerEmpleadosActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(this@VerEmpleadosActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@VerEmpleadosActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@VerEmpleadosActivity,"Fallo al traer el item o se esta utilizando en otro registro",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //Enviar datos
    fun enviar(context: Context, listaEmpleados : List<EmpleadosDataCollectionItem>?, position :Int){
        val intent = Intent(context,RegistrarEmpleadosActivity::class.java)
        intent.putExtra("empleado",listaEmpleados!![position])
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}