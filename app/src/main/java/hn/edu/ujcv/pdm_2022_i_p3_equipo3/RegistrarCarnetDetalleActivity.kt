package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.SpinnerAdapterIDString
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCarnetDetalleBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.*
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.*
import kotlinx.android.synthetic.main.content_registrar_civil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList

class RegistrarCarnetDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarCarnetDetalleBinding
    private val formatDate = SimpleDateFormat("yyyy-MM-dd",Locale.US)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCarnetDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCarnetDetalle)
        callServiceGetCarnetEncabezado()
        callServiceGetEmpleados()
        callServiceGetUnidadVacunacion()
        callServiceGetCarnetDetalle()
        spinnerDosis()
        datePickerMetodo()
        validarDetalle()
        binding.contentCarnetDetalle.btnVerListaDetalles.setOnClickListener {
            val intent = Intent(this,VerCarnetDetalleActivity::class.java)
            startActivity(intent)
        }
        binding.contentCarnetDetalle.btnRegistrarDetalleD.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePostCarnetDetalle()
            }
        }
        binding.contentCarnetDetalle.imvButtonActualizar.setOnClickListener {
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePutCarnetDetalle()
            }
        }
    }
    private fun itemSpinner(spinner: Spinner): SpinnerAdapterIDString {
        return spinner.selectedItem as SpinnerAdapterIDString
    }
    private fun spinnerDosis(){
        val spinner = findViewById<Spinner>(R.id.spnDosisDetalle)
        val lista = resources.getStringArray(R.array.spnDosis)
        val adaptador = ArrayAdapter(this, android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    fun spinnerUnidades(lista: ArrayList<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnUnidadDetalle)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    fun spinnerEncabezados(lista: ArrayList<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnIdDetalle)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    fun spinnerEmpleados(lista: ArrayList<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnIDVacunador)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    fun spinnerIdDetalle(lista: ArrayList<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnCarnetDetalleID)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    private fun callServiceGetUnidadVacunacion() {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<List<UnidadDataCollectionItem>> = unidadService.listUnidades()
        result.enqueue(object: Callback<List<UnidadDataCollectionItem>> {
            override fun onFailure(call: Call<List<UnidadDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<UnidadDataCollectionItem>>,
                response: Response<List<UnidadDataCollectionItem>>
            ) {
                val unidades = arrayListOf<SpinnerAdapterIDString>()
                for(item in response.body()!!) {
                    unidades.add(SpinnerAdapterIDString(item.id_unidad!!.toLong(),item.tipo))
                }
                spinnerUnidades(unidades)
            }
        })
    }
    private fun callServiceGetCarnetEncabezado() {
        val carnetService: CarnetEncabezadoService = RestEngine.buildService().create(CarnetEncabezadoService::class.java)
        val result: Call<List<CarnetEncabezadoDataCollectionItem>> = carnetService.listCarnetEncabezado()
        result.enqueue(object: Callback<List<CarnetEncabezadoDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetEncabezadoDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetEncabezadoDataCollectionItem>>,
                response: Response<List<CarnetEncabezadoDataCollectionItem>>
            ) {
                val encabezados = arrayListOf<SpinnerAdapterIDString>()
                for(item in response.body()!!) {
                    encabezados.add(SpinnerAdapterIDString(item.id_carnetEncabezado!!.toLong(),item.numeroCarnet.toString()))
                }
                spinnerEncabezados(encabezados)
            }
        })
    }
    private fun callServiceGetEmpleados() {
        val empleadoService: EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<List<EmpleadosDataCollectionItem>> = empleadoService.listEmpleados()
        result.enqueue(object: Callback<List<EmpleadosDataCollectionItem>> {
            override fun onFailure(call: Call<List<EmpleadosDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<EmpleadosDataCollectionItem>>,
                response: Response<List<EmpleadosDataCollectionItem>>
            ) {
                val empleados = arrayListOf<SpinnerAdapterIDString>()
                for(item in response.body()!!) {
                    empleados.add(SpinnerAdapterIDString(item.id_empleado!!.toLong(),item.nombre))
                }
                spinnerEmpleados(empleados)
            }
        })
    }
    private fun callServiceGetCarnetDetalle() {
        val detalleService: CarnetDetalleService = RestEngine.buildService().create(
            CarnetDetalleService::class.java)
        val result: Call<List<CarnetDetallesDataCollectionItem>> = detalleService.listCarnetDetalle()
        result.enqueue(object: Callback<List<CarnetDetallesDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetDetallesDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetDetallesDataCollectionItem>>,
                response: Response<List<CarnetDetallesDataCollectionItem>>
            ) {
                val detalles = arrayListOf<SpinnerAdapterIDString>()
                for(item in response.body()!!) {
                    detalles.add(SpinnerAdapterIDString(item.id_carnetDetalle!!.toLong(),item.id_carnetDetalle.toString()))
                }
                spinnerIdDetalle(detalles)
            }
        })
    }
    private fun datePickerMetodo(){
        binding.contentCarnetDetalle.txtFechaCarnetD.setOnClickListener(View.OnClickListener {
            val getDate:Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,i)
                selectedDate.set(Calendar.MONTH,i2)
                selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectedDate.time)
                binding.contentCarnetDetalle.txtFechaCarnetD.setText(date)

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        })
    }
    private fun callServicePostCarnetDetalle() {
        val detalleInfo = CarnetDetallesDataCollectionItem(
            id_carnetDetalle = null,
            id_carnetEncabezado = itemSpinner(binding.contentCarnetDetalle.spnIdDetalle).id.toInt(),
            id_unidad = itemSpinner(binding.contentCarnetDetalle.spnUnidadDetalle).id.toInt(),
            fecha = binding.contentCarnetDetalle.txtFechaCarnetD.text.toString(),
            dosis = binding.contentCarnetDetalle.spnDosisDetalle.selectedItem.toString(),
            observacion = binding.contentCarnetDetalle.editTextTextPersonName6.text.toString(),
            idEmpleadoVacuno = itemSpinner(binding.contentCarnetDetalle.spnIDVacunador).id.toInt()
        )
        addDetalle(detalleInfo){
            if (it?.id_carnetDetalle != null){
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Se ha Registrado correctamente"+ it.id_carnetDetalle, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error" + it?.id_carnetDetalle, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    private fun addDetalle(detalle:CarnetDetallesDataCollectionItem, onResult:(CarnetDetallesDataCollectionItem?)->Unit){
        val detalleService:CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<CarnetDetallesDataCollectionItem> = detalleService.addCarnetDetalle(detalle)
        result.enqueue(object: Callback<CarnetDetallesDataCollectionItem>{
            override fun onFailure(call: Call<CarnetDetallesDataCollectionItem>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse(
                call: Call<CarnetDetallesDataCollectionItem>,
                response: Response<CarnetDetallesDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        val addedDetalle = response.body()!!
                        onResult(addedDetalle)
                    }
                    response.code()==500 -> {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@RegistrarCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@RegistrarCarnetDetalleActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    //
    private fun callServicePutCarnetDetalle() {
        val detalleInfo = CarnetDetallesDataCollectionItem(
            id_carnetDetalle = itemSpinner(binding.contentCarnetDetalle.spnCarnetDetalleID).id.toInt(),
            id_carnetEncabezado = itemSpinner(binding.contentCarnetDetalle.spnIdDetalle).id.toInt(),
            id_unidad = itemSpinner(binding.contentCarnetDetalle.spnUnidadDetalle).id.toInt(),
            fecha = binding.contentCarnetDetalle.txtFechaCarnetD.text.toString(),
            dosis = binding.contentCarnetDetalle.spnDosisDetalle.selectedItem.toString(),
            observacion = binding.contentCarnetDetalle.editTextTextPersonName6.text.toString(),
            idEmpleadoVacuno = itemSpinner(binding.contentCarnetDetalle.spnIDVacunador).id.toInt()
        )
        val detalleService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<CarnetDetallesDataCollectionItem> = detalleService.updateCarnetDetalle(detalleInfo)
        result.enqueue(
            object : Callback<CarnetDetallesDataCollectionItem>{
                override fun onFailure(call: Call<CarnetDetallesDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarCarnetDetalleActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CarnetDetallesDataCollectionItem>,
                    response: Response<CarnetDetallesDataCollectionItem>
                ) {
                    when {
                        response.isSuccessful -> {
                            val updateDetalle = response.body()!!
                            Toast.makeText(this@RegistrarCarnetDetalleActivity,"Actualizado exitosamente :" + updateDetalle.id_carnetDetalle,Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarCarnetDetalleActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarCarnetDetalleActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //

    fun validarEntradas():Boolean{
        return when {
            binding.contentCarnetDetalle.txtFechaCarnetD.text.isNullOrEmpty() -> true
            binding.contentCarnetDetalle.editTextTextPersonName6.text.isNullOrEmpty() -> true
            !binding.contentCarnetDetalle.editTextTextPersonName6.error.isNullOrEmpty() -> true
            else -> false
        }
    }
    fun validarDetalle(){
        binding.contentCarnetDetalle.editTextTextPersonName6.doAfterTextChanged {
            if (binding.contentCarnetDetalle.editTextTextPersonName6.text.trim().isEmpty()) {
                binding.contentCarnetDetalle.editTextTextPersonName6.error="La observacion no debe estar vacia"
            }
            if (binding.contentCarnetDetalle.editTextTextPersonName6.text.trim().length < 2) {
                binding.contentCarnetDetalle.editTextTextPersonName6.error="Ingrese mas de dos caracteres en la observacion"
            }
            if (binding.contentCarnetDetalle.editTextTextPersonName6.text.length > 50) {
                binding.contentCarnetDetalle.editTextTextPersonName6.error="La observacion es demasiado larga"
            }
        }
    }

}