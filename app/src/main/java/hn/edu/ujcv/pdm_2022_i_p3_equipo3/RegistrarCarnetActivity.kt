package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.SpinnerAdapterIDString
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCarnetBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.*
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarCarnetActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>? = null
    private lateinit var binding: ActivityRegistrarCarnetBinding
    private var listaDetalles = arrayListOf<CarnetDetallesDataCollectionItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCarnetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCarnet)
        //---------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnetEncabezado.recyclerDetallesC.layoutManager = layoutManager
        binding.contentCarnetEncabezado.txtIdEncabezado.setText("0")
        callServiceGetCiviles()
        obtenerCarnetEncabezado()
        if(!binding.contentCarnetEncabezado.txtIdEncabezado.text.isNullOrEmpty()){
            callServiceGetCarnetDetalle()
        }else{
            actualizarRecyclerView(null)
        }
        validarEncabezado()
        ///
        binding.contentCarnetEncabezado.btnAgregarDetalle.setOnClickListener {
            if(!binding.contentCarnetEncabezado.txtIdEncabezado.text.isNullOrEmpty()){
                val intent = Intent(this,RegistrarCarnetDetalleActivity::class.java)
                intent.putExtra("idEncabezado",binding.contentCarnetEncabezado.txtIdEncabezado.text.toString().toInt())
                startActivity(intent)
            }
        }
        binding.contentCarnetEncabezado.btnRegistrarCarnetEncabezado.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCarnetActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePostCarnetEncabezado()
            }
        }
        binding.contentCarnetEncabezado.btnActualizarCarnetEncabezado.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCarnetActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePutCarnetEncabezado()
            }
        }

    }
    fun actualizarRecyclerView(lista:ArrayList<CarnetDetallesDataCollectionItem>?){
        adapter = RecyclerAdapterCarnetDetalle(lista)
        binding.contentCarnetEncabezado.recyclerDetallesC.adapter = adapter
    }
    private fun enviar(){
        val intent = Intent(this,VerCarnetVacunacionActivity::class.java)
        startActivity(intent)
    }
    private fun obtenerCarnetEncabezado() {
        val intent = intent
        val objeto: CarnetEncabezadoDataCollectionItem? = intent.getParcelableExtra("encabezado")
        if (objeto != null) {
            binding.contentCarnetEncabezado.txtNumeroCarnet.setText(objeto.numeroCarnet.toString())
            binding.contentCarnetEncabezado.txtIdEncabezado.setText(objeto.id_carnetEncabezado.toString())
        }
    }
    private fun callServiceGetCiviles() {
        val civilService: CivilesService = RestEngine.buildService().create(CivilesService::class.java)
        val result: Call<List<CivilDataCollectionItem>> = civilService.listCiviles()
        result.enqueue(
            object : Callback<List<CivilDataCollectionItem>> {
                override fun onFailure(call: Call<List<CivilDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistrarCarnetActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CivilDataCollectionItem>>,
                    response: Response<List<CivilDataCollectionItem>>
                ) {

                    val civiles = arrayListOf<SpinnerAdapterIDString>()
                    for(item in response.body()!!) {
                        civiles.add(SpinnerAdapterIDString(item.id_civil!!.toLong(),item.nombre))
                    }
                    spinnerPacientes(civiles)
                }
            }
        )
    }
    private fun callServicePostCarnetEncabezado() {
        val encabezadoInfo = CarnetEncabezadoDataCollectionItem(
            id_carnetEncabezado = null,
            numeroCarnet = binding.contentCarnetEncabezado.txtNumeroCarnet.text.toString().toLong(),
            id_civil = itemSpinner(binding.contentCarnetEncabezado.spnPacienteEncabezado).id
        )
        addEncabezado(encabezadoInfo){
            if (it?.id_carnetEncabezado != null){
                Toast.makeText(this@RegistrarCarnetActivity,"Se ha Registrado correctamente"+ it.numeroCarnet, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegistrarCarnetActivity,"Error" + it?.id_carnetEncabezado, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun addEncabezado(encabezado: CarnetEncabezadoDataCollectionItem, onResult:(CarnetEncabezadoDataCollectionItem?)->Unit){
        val encabezadoService: CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<CarnetEncabezadoDataCollectionItem> = encabezadoService.addCarnetEncabezado(encabezado)
        result.enqueue(object: Callback<CarnetEncabezadoDataCollectionItem> {
            override fun onFailure(call: Call<CarnetEncabezadoDataCollectionItem>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse(
                call: Call<CarnetEncabezadoDataCollectionItem>,
                response: Response<CarnetEncabezadoDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        val addedCargo = response.body()!!
                        onResult(addedCargo)
                    }
                    response.code()==500 -> {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@RegistrarCarnetActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@RegistrarCarnetActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun callServicePutCarnetEncabezado() {
        val encabezadoInfo = CarnetEncabezadoDataCollectionItem(
            id_carnetEncabezado = binding.contentCarnetEncabezado.txtIdEncabezado.text.toString().toLong(),
            numeroCarnet = binding.contentCarnetEncabezado.txtNumeroCarnet.text.toString().toLong(),
            id_civil = itemSpinner(binding.contentCarnetEncabezado.spnPacienteEncabezado).id
        )
        val encabezadoService: CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<CarnetEncabezadoDataCollectionItem> = encabezadoService.updateCarnetEncabezado(encabezadoInfo)
        result.enqueue(
            object : Callback<CarnetEncabezadoDataCollectionItem> {
                override fun onFailure(call: Call<CarnetEncabezadoDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarCarnetActivity,"Error:", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CarnetEncabezadoDataCollectionItem>,
                    response: Response<CarnetEncabezadoDataCollectionItem>
                ) {
                    when {
                        response.isSuccessful -> {
                            val updateEmpleado = response.body()!!
                            Toast.makeText(this@RegistrarCarnetActivity,"Actualizado exitosamente :" + updateEmpleado.numeroCarnet,
                                Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarCarnetActivity,"Sesion Expirada", Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarCarnetActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarCarnetActivity,"Fallo al obtener el item",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    fun spinnerPacientes(lista: List<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnPacienteEncabezado)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }

    private fun itemSpinner(spinner: Spinner): SpinnerAdapterIDString {
        return spinner.selectedItem as SpinnerAdapterIDString
    }
    fun validarEntradas():Boolean{
        return when{
            binding.contentCarnetEncabezado.txtNumeroCarnet.text.isNullOrEmpty() -> true
            !binding.contentCarnetEncabezado.txtNumeroCarnet.error.isNullOrEmpty() -> true
            else -> false
        }
    }
    fun callServiceGetCarnetDetalle() {
        val detalleService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<List<CarnetDetallesDataCollectionItem>> = detalleService.listCarnetDetalle()
        result.enqueue(object: Callback<List<CarnetDetallesDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetDetallesDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarCarnetActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetDetallesDataCollectionItem>>,
                response: Response<List<CarnetDetallesDataCollectionItem>>
            ) {
                val detalles = response.body()!!
                for(item in detalles){
                    if(item.id_carnetEncabezado == binding.contentCarnetEncabezado.txtIdEncabezado.text.toString().toInt()){
                        listaDetalles!!.add(item)
                    }
                }
                actualizarRecyclerView(listaDetalles)
            }
        })
    }
    fun callServiceDeleteCarnetDetalle(listaCarnet : List<CarnetDetallesDataCollectionItem>?, position: Int) {
        val carnetService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<ResponseBody> = carnetService.deleteCarnetDetalle(listaCarnet!![position].id_carnetDetalle!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@RegistrarCarnetActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(this@RegistrarCarnetActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarCarnetActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarCarnetActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,VerCarnetVacunacionActivity::class.java)
        startActivity(intent)
    }
    fun validarEncabezado(){
        binding.contentCarnetEncabezado.txtNumeroCarnet.doAfterTextChanged {
            if(binding.contentCarnetEncabezado.txtNumeroCarnet.text.toString().isNullOrEmpty()){
                binding.contentCarnetEncabezado.txtNumeroCarnet.error = "El número de carnet esta vacío"
            }
            if(binding.contentCarnetEncabezado.txtNumeroCarnet.text.toString().length < 3 ){
                binding.contentCarnetEncabezado.txtNumeroCarnet.error ="El número de carnet debe tener al menos 3 digitos"
            }
            if(binding.contentCarnetEncabezado.txtNumeroCarnet.text.toString().length > 5){
                binding.contentCarnetEncabezado.txtNumeroCarnet.error ="El número de carnet es muy largo"
            }
        }
    }

}