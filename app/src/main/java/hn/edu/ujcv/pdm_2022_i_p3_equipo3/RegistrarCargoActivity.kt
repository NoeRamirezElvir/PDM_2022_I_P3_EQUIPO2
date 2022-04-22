package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
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
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCargoBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CargosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EmpleadosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarCargoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarCargoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCargoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCargo)
        ////
        obtenerCargo()
        validacionesCargo()
        binding.contentCargosRegistrar.btnRegistrarCargo.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCargoActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePostCargo()
            }
        }
        binding.contentCargosRegistrar.btnActualizarCargo.setOnClickListener{
            if(validarEntradas()){
                Toast.makeText(this@RegistrarCargoActivity,"Compruebe los datos", Toast.LENGTH_LONG).show()
            }else{
                callServicePutCargo()
            }
        }


    }
    private fun enviar(){
        val intent = Intent(this,VerCargosActivity::class.java)
        startActivity(intent)
    }
    private fun obtenerCargo() {
        val intent = intent
        val objeto: CargosDataCollectionItem? = intent.getParcelableExtra("cargo")
        if (objeto != null) {
            binding.contentCargosRegistrar.txtIdCargo.setText(objeto.id_cargo!!.toString())
            binding.contentCargosRegistrar.txtNombreCargo.setText(objeto.nombre.toString())
            binding.contentCargosRegistrar.txtDescripcionCargo.setText(objeto.descripcion)
        }
    }
    private fun callServicePostCargo() {
        val cargoInfo = CargosDataCollectionItem(
            id_cargo = null,
            nombre = capitalize(binding.contentCargosRegistrar.txtNombreCargo.text.toString().trim())!!,
            descripcion = binding.contentCargosRegistrar.txtDescripcionCargo.text.toString()
        )
        addCargo(cargoInfo){
            if (it?.id_cargo != null){
                Toast.makeText(this@RegistrarCargoActivity,"Se ha Registrado correctamente"+ it.nombre, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegistrarCargoActivity,"Error" + it?.id_cargo, Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun addCargo(cargo:CargosDataCollectionItem, onResult:(CargosDataCollectionItem?)->Unit){
        val cargoService: CargosService = RestEngine.buildService().create(CargosService::class.java)
        val result: Call<CargosDataCollectionItem> = cargoService.addCargo(cargo)
        result.enqueue(object: Callback<CargosDataCollectionItem>{
            override fun onFailure(call: Call<CargosDataCollectionItem>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse(
                call: Call<CargosDataCollectionItem>,
                response: Response<CargosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        val addedCargo = response.body()!!
                        onResult(addedCargo)
                    }
                    response.code()==500 -> {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@RegistrarCargoActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@RegistrarCargoActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    private fun callServicePutCargo() {
        val cargoInfo = CargosDataCollectionItem(
            id_cargo = binding.contentCargosRegistrar.txtIdCargo.text.toString().toInt(),
            nombre = capitalize(binding.contentCargosRegistrar.txtNombreCargo.text.toString().trim())!!,
            descripcion = binding.contentCargosRegistrar.txtDescripcionCargo.text.toString()
        )
        val cargosService: CargosService = RestEngine.buildService().create(CargosService::class.java)
        val result: Call<CargosDataCollectionItem> = cargosService.updateCargo(cargoInfo)
        result.enqueue(
            object : Callback<CargosDataCollectionItem>{
                override fun onFailure(call: Call<CargosDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarCargoActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CargosDataCollectionItem>,
                    response: Response<CargosDataCollectionItem>
                ) {
                    when {
                        response.isSuccessful -> {
                            val updateEmpleado = response.body()!!
                            Toast.makeText(this@RegistrarCargoActivity,"Actualizado exitosamente :" + updateEmpleado.nombre,Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarCargoActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarCargoActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarCargoActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    fun validarEntradas():Boolean{
        return when{
            binding.contentCargosRegistrar.txtNombreCargo.text.isNullOrEmpty() -> true
            !binding.contentCargosRegistrar.txtNombreCargo.error.isNullOrEmpty() -> true
            binding.contentCargosRegistrar.txtDescripcionCargo.text.isNullOrEmpty() -> true
            !binding.contentCargosRegistrar.txtDescripcionCargo.error.isNullOrEmpty() -> true
            else -> false
        }
    }
    fun validacionesCargo(){
        binding.contentCargosRegistrar.txtNombreCargo.doAfterTextChanged {
            if(binding.contentCargosRegistrar.txtNombreCargo.text.isNullOrEmpty()){
                binding.contentCargosRegistrar.txtNombreCargo.error ="El nombre del empleado esta vacío"
            }
            if(binding.contentCargosRegistrar.txtNombreCargo.text.toString().length < 3){
                binding.contentCargosRegistrar.txtNombreCargo.error ="Ingrese mas de 3 caracteres en el nombre"
            }
            if(binding.contentCargosRegistrar.txtNombreCargo.text.toString().length > 30){
                binding.contentCargosRegistrar.txtNombreCargo.error ="El nombre es muy largo"
            }
        }
        binding.contentCargosRegistrar.txtDescripcionCargo.doAfterTextChanged {
            if( binding.contentCargosRegistrar.txtDescripcionCargo.text.isNullOrEmpty()){
                binding.contentCargosRegistrar.txtDescripcionCargo.error = "La descripción esta vacía"
            }
            if(binding.contentCargosRegistrar.txtDescripcionCargo.text.toString().length < 5){
                binding.contentCargosRegistrar.txtDescripcionCargo.error = "Ingrese mas de 5 caracteres en la descripción"
            }
            if(binding.contentCargosRegistrar.txtDescripcionCargo.text.toString().length > 50){
                binding.contentCargosRegistrar.txtDescripcionCargo.error= "La descripción es muy larga"
            }
        }
    }
    private fun capitalize(line: String): String? {
        return Character.toUpperCase(line[0]).toString() + line.substring(1)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        enviar()
    }
}