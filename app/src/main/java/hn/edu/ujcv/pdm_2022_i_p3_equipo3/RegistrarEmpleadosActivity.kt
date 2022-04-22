package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.SpinnerAdapterIDString
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarEmpleadoBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.*
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CargosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EmpleadosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.UnidadService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegistrarEmpleadosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarEmpleadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroEmpleados)
        //---------------------------
        callServiceGetUnidadVacunacion()
        callServiceGetCargos()
        obtenerEmpleado()
        validacionesEmpleado()
        //acceso()
        //
        binding.contentEmpleadoRegistrar.btnRegistrarEnfermero.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarEmpleadosActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePostEmpleado()
            }
        }
        binding.contentEmpleadoRegistrar.btnActualizarEnfermero.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarEmpleadosActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePutEmpleado()
            }
        }

    }
    //Abrir lista
    private fun enviar(){
        val intent = Intent(this,VerEmpleadosActivity::class.java)
        startActivity(intent)
    }
    private fun enviarLogin(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
    //Obtener Informacion
    private fun obtenerEmpleado() {
        val intent = intent
        val objeto:EmpleadosDataCollectionItem? = intent.getParcelableExtra("empleado")
        if (objeto != null) {
            binding.contentEmpleadoRegistrar.txtIdEmpleado.setText(objeto.id_empleado!!.toString())
            binding.contentEmpleadoRegistrar.txtCodigoEmpleado.setText(objeto.codigo.toString())
            binding.contentEmpleadoRegistrar.txtDniEmpleado.setText(objeto.dni)
            binding.contentEmpleadoRegistrar.txtNombreEmpleado.setText(objeto.nombre)
            binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.setText(objeto.telefono.toString())
            binding.contentEmpleadoRegistrar.txtCorreoEmpleado.setText(objeto.correo)
            binding.contentEmpleadoRegistrar.txtPasswordEmpleado.setText(objeto.password)
        }
    }
    //
    private fun callServicePostEmpleado() {
        val empleadoInfo = EmpleadosDataCollectionItem(
            id_empleado = null,
            codigo = binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.toString().toLong(),
            dni = binding.contentEmpleadoRegistrar.txtDniEmpleado.text.toString(),
            nombre = binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.toString().trim(),
            telefono = binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.toString().toLong(),
            correo = binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.toString(),
            password = binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.toString(),
            id_cargo = itemSpinner(binding.contentEmpleadoRegistrar.spnCargoEmpleado).id.toInt(),
            id_unidad = itemSpinner(binding.contentEmpleadoRegistrar.spnUnidadVEmpleado).id.toInt()
        )
        addEmpleado(empleadoInfo){
            if (it?.id_empleado != null){
                Toast.makeText(this@RegistrarEmpleadosActivity,"Se ha Registrado correctamente"+ it.nombre, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegistrarEmpleadosActivity,"Error" + it?.id_empleado, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    private fun addEmpleado(empleado:EmpleadosDataCollectionItem, onResult:(EmpleadosDataCollectionItem?)->Unit){
        val empleadoService:EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadoService.addEmpleado(empleado)
        result.enqueue(object: Callback<EmpleadosDataCollectionItem>{
            override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse(
                call: Call<EmpleadosDataCollectionItem>,
                response: Response<EmpleadosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        val addedEmpleado = response.body()!!
                        onResult(addedEmpleado)
                    }
                    response.code()==500 -> {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@RegistrarEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@RegistrarEmpleadosActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    //
    private fun callServicePutEmpleado() {
        val empleadoInfo = EmpleadosDataCollectionItem(
            id_empleado = binding.contentEmpleadoRegistrar.txtIdEmpleado.text.toString().toInt(),
            codigo = binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.toString().toLong(),
            dni = binding.contentEmpleadoRegistrar.txtDniEmpleado.text.toString(),
            nombre = binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.toString().trim(),
            telefono = binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.toString().toLong(),
            correo = binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.toString(),
            password = binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.toString(),
            id_cargo = itemSpinner(binding.contentEmpleadoRegistrar.spnCargoEmpleado).id.toInt(),
            id_unidad = itemSpinner(binding.contentEmpleadoRegistrar.spnUnidadVEmpleado).id.toInt()
        )
        val empleadosService: EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadosService.updateEmpleado(empleadoInfo)
        result.enqueue(
            object : Callback<EmpleadosDataCollectionItem>{
                override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarEmpleadosActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<EmpleadosDataCollectionItem>,
                    response: Response<EmpleadosDataCollectionItem>
                ) {
                    when {
                        response.isSuccessful -> {
                            val updateEmpleado = response.body()!!
                            Toast.makeText(this@RegistrarEmpleadosActivity,"Actualizado exitosamente :" + updateEmpleado.nombre,Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarEmpleadosActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarEmpleadosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarEmpleadosActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //

    fun spinnerUnidades(lista: ArrayList<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnUnidadVEmpleado)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    fun spinnerCargos(lista: List<SpinnerAdapterIDString>){
        val spinner = findViewById<Spinner>(R.id.spnCargoEmpleado)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista)
        spinner.adapter = adaptador
    }
    private fun callServiceGetUnidadVacunacion() {
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<List<UnidadDataCollectionItem>> = unidadService.listUnidades()
        result.enqueue(object: Callback<List<UnidadDataCollectionItem>> {
            override fun onFailure(call: Call<List<UnidadDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarEmpleadosActivity,"Error", Toast.LENGTH_LONG).show()
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
    private fun callServiceGetCargos() {
        val cargosService: CargosService = RestEngine.buildService().create(CargosService::class.java)
        val result: Call<List<CargosDataCollectionItem>> = cargosService.listCargos()
        result.enqueue(object: Callback<List<CargosDataCollectionItem>> {
            override fun onFailure(call: Call<List<CargosDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarEmpleadosActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CargosDataCollectionItem>>,
                response: Response<List<CargosDataCollectionItem>>
            ) {
                val cargos = arrayListOf<SpinnerAdapterIDString>()
                for(item in response.body()!!) {
                    cargos.add(SpinnerAdapterIDString(item.id_cargo!!.toLong(),item.nombre))
                }
                spinnerCargos(cargos)
            }
        })
    }
    private fun itemSpinner(spinner: Spinner): SpinnerAdapterIDString {
        return spinner.selectedItem as SpinnerAdapterIDString
    }

  fun validarEntradas():Boolean{
      return when{
          binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtCodigoEmpleado.error.isNullOrEmpty() -> true
          binding.contentEmpleadoRegistrar.txtDniEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtDniEmpleado.error.isNullOrEmpty() -> true
          binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtNombreEmpleado.error.isNullOrEmpty() -> true
          binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.error.isNullOrEmpty() -> true
          binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtCorreoEmpleado.error.isNullOrEmpty() -> true
          binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.isNullOrEmpty() -> true
          !binding.contentEmpleadoRegistrar.txtPasswordEmpleado.error.isNullOrEmpty() -> true
          else -> false
      }
  }

    fun validacionesEmpleado(){
        binding.contentEmpleadoRegistrar.txtCodigoEmpleado.doAfterTextChanged {
            if (binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.isNullOrEmpty()){
                binding.contentEmpleadoRegistrar.txtCodigoEmpleado.error = "Codigo no debe estar vacío"
            }
            if (binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.toString().length > 5){
                binding.contentEmpleadoRegistrar.txtCodigoEmpleado.error ="Codigo demasiado largo"
            }
            if (binding.contentEmpleadoRegistrar.txtCodigoEmpleado.text.toString().length<3){
                binding.contentEmpleadoRegistrar.txtCodigoEmpleado.error ="Codigo demasiado corto"
            }
        }
        binding.contentEmpleadoRegistrar.txtDniEmpleado.doAfterTextChanged {
            if (binding.contentEmpleadoRegistrar.txtDniEmpleado.text.isNullOrEmpty()){
                binding.contentEmpleadoRegistrar.txtDniEmpleado.error ="DNI no debe estar vacío"
            }
            if (binding.contentEmpleadoRegistrar.txtDniEmpleado.text.toString().length > 13){
                binding.contentEmpleadoRegistrar.txtDniEmpleado.error ="DNI demasiado largo"
            }
            if (binding.contentEmpleadoRegistrar.txtDniEmpleado.text.toString().length < 13){
                binding.contentEmpleadoRegistrar.txtDniEmpleado.error ="DNI demasiado corto"
            }
        }
        binding.contentEmpleadoRegistrar.txtNombreEmpleado.doAfterTextChanged {
            if(binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.isNullOrEmpty()){
                binding.contentEmpleadoRegistrar.txtNombreEmpleado.error ="El nombre del empleado esta vacío"
            }
            if(binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.toString().length < 3){
                binding.contentEmpleadoRegistrar.txtNombreEmpleado.error ="Ingrese mas de 3 caracteres en el nombre"
            }
            if(binding.contentEmpleadoRegistrar.txtNombreEmpleado.text.toString().length > 40){
                binding.contentEmpleadoRegistrar.txtNombreEmpleado.error ="El nombre es muy largo"
            }
        }
        binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.doAfterTextChanged {
            if (binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.isNullOrEmpty()) {
                binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.error ="El telefono no debe estar vacío"
            }
            if(!binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.isNullOrEmpty()){
                if (binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.toString().length != 8) {
                    binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.error ="No. telefono Invalido"
                }
                val telefono= binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.text.toString()
                if (telefono[0] != '2' && telefono[0] != '9' && telefono[0] != '8' && telefono[0] != '3' && telefono[0] !='7') {
                    binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.error ="Operador de telefono Invalido!"
                }
                val patron = Pattern.compile("[23789]")
                val validarNumero = patron.matcher(java.lang.String.valueOf(telefono).substring(0, 1))
                if (!validarNumero.matches()) {
                    binding.contentEmpleadoRegistrar.txtTelefonoEmpleado.error ="El número de teléfono debe iniciar con 2,3,7,8 o 9"
                }
            }
        }
        binding.contentEmpleadoRegistrar.txtCorreoEmpleado.doAfterTextChanged {
            if(binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.isNullOrEmpty()){
                binding.contentEmpleadoRegistrar.txtCorreoEmpleado.error ="El correo del empleado esta vacío"
            }
            if(binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.toString().length < 3){
                binding.contentEmpleadoRegistrar.txtCorreoEmpleado.error ="Ingrese mas de 3 caracteres en el correo"
            }
            if(binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.toString().length > 50) {
                binding.contentEmpleadoRegistrar.txtCorreoEmpleado.error ="El correo es muy largo"
            }
            if (!validarCorreo(binding.contentEmpleadoRegistrar.txtCorreoEmpleado.text.toString())){
                binding.contentEmpleadoRegistrar.txtCorreoEmpleado.error ="La direccion de correo es invalida"
            }
        }
        binding.contentEmpleadoRegistrar.txtPasswordEmpleado.doAfterTextChanged {
            if (binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.isNullOrEmpty()) {
                binding.contentEmpleadoRegistrar.txtPasswordEmpleado.error ="La contraseña no debe estar vacia"
            }
            if (binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.toString().length < 5) {
                binding.contentEmpleadoRegistrar.txtPasswordEmpleado.error ="Ingrese mas de cinco caracteres en la contraseña"
            }
            if (binding.contentEmpleadoRegistrar.txtPasswordEmpleado.text.toString().length > 15) {
                binding.contentEmpleadoRegistrar.txtPasswordEmpleado.error ="La contraseña es demasiado larga"
            }
        }
    }
    private fun validarCorreo(correo:String):Boolean{
        val  pattern: Pattern = Pattern.compile( "(\\W|^)[\\w.\\-]{3,25}@(hotmail|gmail|ujcv|msn|outlook|yahoo|gmx|zoho|tutonota|protonmail)\\.(com|es|org|edu|hn|uk|co|info|net|site)(\\W|$)")
        val matcher: Matcher = pattern.matcher(correo)
        return matcher.find()
    }
    fun acceso(){
        val acceso:Boolean = intent.getBooleanExtra("acceso",true)
        if(acceso){
            enviar()
        }else{
            enviarLogin()
        }
    }
    override fun onBackPressed() {
        super.onBackPressed()
        acceso()
    }

}