package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CargosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EmpleadosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ver_empleado.*
import kotlinx.android.synthetic.main.login_cambiarcontra.*
import kotlinx.android.synthetic.main.login_cambiarcontra.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private  var empleado:EmpleadosDataCollectionItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        logIn()

    }
    private fun validarHelperText(){
        edittxtCodigoUsuario.doAfterTextChanged {
            when{
                edittxtCodigoUsuario.text.isNullOrEmpty() -> txtCodigoUsuario.error = "El nombre esta vacío"
                edittxtCodigoUsuario.text.toString().length < 3-> txtCodigoUsuario.error = "El nombre es muy corto"
                edittxtCodigoUsuario.text.toString().length >50-> txtCodigoUsuario.error = "El nombre es muy largo"
                else -> txtCodigoUsuario.error= ""
            }
        }
        edittxtContraUsuario.doAfterTextChanged {
            when{
                edittxtContraUsuario.text.isNullOrEmpty()->txtContraseUsuario.error = "La contraseña esta vacía"
                edittxtContraUsuario.text.toString().length < 5 -> txtContraseUsuario.error = "La contraseña es muy corta"
                edittxtContraUsuario.text.toString().length >15 -> txtContraseUsuario.error = "La contraseña es muy larga"
                else->txtContraseUsuario.error = ""
            }
        }
    }
    fun enviar(){
        val intent= Intent(this,MenuPrincipalActivity::class.java)
        intent.putExtra("usuario",empleado)
        startActivity(intent)
        this.finish()
    }
    fun validarEntradas():Boolean{
        return when{
            edittxtCodigoUsuario.text.isNullOrEmpty()->true
            !txtCodigoUsuario.helperText.isNullOrEmpty()->true
            edittxtContraUsuario.text.isNullOrEmpty()->true
            !txtContraseUsuario.helperText.isNullOrEmpty()->true
            else->false
        }
    }
    private fun logIn(){
        validarHelperText()
        btnIniciarSesion.setOnClickListener {
            if(validarEntradas()){
                Toast.makeText(this@MainActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServiceGetEmpleadoNombre()
            }
        }
        txtRegistrarUsuarioAcceso.setOnClickListener{
            //validarHelperText()
            registrar()
        }
        txvRecuperarContra.setOnClickListener {
            //validarHelperText()
            dialogEditar() }
    }
    private fun callServiceGetEmpleadoNombre() {
        val empleadoService:EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadoService.getEmpleadoByNombre(edittxtCodigoUsuario.text.toString())
        result.enqueue(object: Callback<EmpleadosDataCollectionItem>{
            override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<EmpleadosDataCollectionItem>,
                response: Response<EmpleadosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        if((edittxtCodigoUsuario.text.toString() == response.body()!!.nombre)&&
                            (edittxtContraUsuario.text.toString() == response.body()!!.password)){
                            empleado = response.body()!!
                            callServiceGetCargoID(empleado!!.id_cargo)
                        }else{
                            dialogo("Usuario o contraseña son invalidos, por favor compruebe los datos.")
                        }
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@MainActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@MainActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun registrar(){
        val intent = Intent(this,RegistrarEmpleadosActivity::class.java)
        intent.putExtra("acceso",false)
        startActivity(intent)
        this.finish()
    }
    private fun dialogo(m:String){
        val dialog = AlertDialog.Builder(this)
            .setTitle("Login")
            .setMessage(m).setIcon(R.drawable.ic_user)
            .setPositiveButton("OK"){_,_ ->
            }.create()
        dialog.show()
    }
    private fun callServiceGetCargoID(id:Int) {
        val cargosService: CargosService =
            RestEngine.buildService().create(CargosService::class.java)
        val result: Call<CargosDataCollectionItem> =
            cargosService.getCargoById(id)
        result.enqueue(object : Callback<CargosDataCollectionItem> {
            override fun onFailure(call: Call<CargosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CargosDataCollectionItem>,
                response: Response<CargosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        if(response.body()!!.nombre == "Enfermero" || response.body()!!.nombre == "Doctor" ||
                            response.body()!!.nombre == "Administrador"){
                            enviar()
                        }else{
                            dialogo("No cuenta con los permisos necesarios para acceder.")
                        }
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@MainActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@MainActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@MainActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun dialogEditar(){
        txvRecuperarContra.setOnClickListener{
            val dialog = LayoutInflater.from(this).inflate(R.layout.login_cambiarcontra,null)
            val builder = AlertDialog.Builder(this).setView(dialog)
                .setTitle("Cambiar Contraseña")
            val alertDialog = builder.show()
            validacionesDialog(alertDialog)
            alertDialog.dialogAceptar.setOnClickListener {
                alertDialog.dismiss()
                callServiceGetEmpleadoNombre2(dialog.dialogNombre.text.toString(),alertDialog)
            }
            alertDialog.dialogCancelar.setOnClickListener {
                alertDialog.dismiss()
            }

        }

    }
    private fun callServicePutEmpleado(empleado:EmpleadosDataCollectionItem?) {
        val empleadoInfo = empleado!!
        val empleadosService: EmpleadosService = RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadosService.updateEmpleado(empleadoInfo)
        result.enqueue(
            object : Callback<EmpleadosDataCollectionItem>{
                override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@MainActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<EmpleadosDataCollectionItem>,
                    response: Response<EmpleadosDataCollectionItem>
                ) {
                    when {
                        response.isSuccessful -> {
                            val updateEmpleado = response.body()!!
                            Toast.makeText(this@MainActivity,"Actualizado exitosamente :" + updateEmpleado.nombre,Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@MainActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@MainActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@MainActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    private fun callServiceGetEmpleadoNombre2(nombre:String,dialog: AlertDialog) {
        val empleadoService: EmpleadosService =
            RestEngine.buildService().create(EmpleadosService::class.java)
        val result: Call<EmpleadosDataCollectionItem> = empleadoService.getEmpleadoByNombre(nombre)
        result.enqueue(object : Callback<EmpleadosDataCollectionItem> {
            override fun onFailure(call: Call<EmpleadosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<EmpleadosDataCollectionItem>,
                response: Response<EmpleadosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        if(dialog.dialogPassword.text.toString() == dialog.dialogPassword2.text.toString()){
                            empleado = response.body()!!
                            empleado!!.nombre = dialog.dialogNombre.text.toString()
                            empleado!!.password = dialog.dialogPassword.text.toString()
                        }
                        callServicePutEmpleado(empleado)
                    }
                    response.code() == 500 -> {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.string(),
                            RestApiError::class.java
                        )
                        Toast.makeText(
                            this@MainActivity,
                            errorResponse.errorDetails,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.string(),
                            RestApiError::class.java
                        )
                        Toast.makeText(
                            this@MainActivity,
                            errorResponse.errorDetails,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else -> {
                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${response.errorBody()!!.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        })
    }
    fun validacionesDialog(alertDialog: AlertDialog):Boolean{
        alertDialog.dialogNombre.doAfterTextChanged {
            if(!alertDialog.dialogNombre.text.isNullOrEmpty()){
                if(alertDialog.dialogNombre.text.toString().length < 3)
                    alertDialog.dialogNombre.error = "El nombre es muy corto"
                if(alertDialog.dialogNombre.text.toString().length >50)
                    alertDialog.dialogNombre.error = "El nombre es muy largo"
            }else{
                alertDialog.dialogNombre.error = "El nombre esta vacío"
            }
        }
        alertDialog.dialogPassword.doAfterTextChanged {
            if(!alertDialog.dialogPassword.text.isNullOrEmpty()){
                if(alertDialog.dialogPassword.text.toString().length < 5)
                    alertDialog.dialogPassword.error = "La contraseña es muy corta"
                if(alertDialog.dialogPassword.text.toString().length >15)
                    alertDialog.dialogPassword.error = "La contraseña es muy larga"
            }else{
                alertDialog.dialogPassword.error = "La contraseña esta vacía"
            }
        }
        alertDialog.dialogPassword2.doAfterTextChanged {
            if(!alertDialog.dialogPassword2.text.isNullOrEmpty()){
                if(alertDialog.dialogPassword2.text.toString().length < 5)
                    alertDialog.dialogPassword2.error = "La contraseña es muy corta"
                if(alertDialog.dialogPassword2.text.toString().length >15)
                    alertDialog.dialogPassword2.error = "La contraseña es muy larga"
                if(alertDialog.dialogPassword.text.toString() != alertDialog.dialogPassword2.text.toString())
                    alertDialog.dialogPassword2.error = "La contraseña no coincide"
            }else{
                alertDialog.dialogPassword2.error = "La confirmacion de contraseña esta vacía"
            }
        }

       return when{
            alertDialog.dialogNombre.text.isNullOrEmpty()->true
           !alertDialog.dialogNombre.error.isNullOrEmpty()->true
           alertDialog.dialogPassword.text.isNullOrEmpty()->true
           !alertDialog.dialogPassword.error.isNullOrEmpty()->true
           alertDialog.dialogPassword2.text.isNullOrEmpty()->true
           !alertDialog.dialogPassword2.error.isNullOrEmpty()->true
            else->false
        }
    }
}