package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCivilBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollection
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_civil.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegistrarCivilActivity : AppCompatActivity() {
    private val formatDate = SimpleDateFormat("yyyy-MM-dd",Locale.US)
    private lateinit var binding: ActivityRegistrarCivilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCivilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCiviles)
        //-----------------------------------
        spinner()
        datePickerMetodo()
        btnRegistrarCivil.setOnClickListener { v ->
            if (txtDniCivil.text.isNullOrEmpty()){
                Toast.makeText(this@RegistrarCivilActivity,"Dni esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePostCivil()
        }
        btnActualizarCivil.setOnClickListener { v->
            if (txtIdCivil.text.isNullOrEmpty() || txtDniCivil.text.isNullOrEmpty() ){
                Toast.makeText(this@RegistrarCivilActivity,"Datos del paciente estan vacíos",Toast.LENGTH_LONG).show()
            }else
                callServicePutCivil()
        }
        obtener()
    }

    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("civil") as CivilDataCollectionItem?
        if (objeto != null) {
            txtIdCivil.setText(objeto!!.id_civil.toString())
            //spnSexoCivil.a ( objeto!!.id_civil.toString())
            txtDniCivil.setText(objeto!!.dni.toString())
            txtNombreCivil.setText(objeto!!.nombre)
            txtTelefonoCivil.setText(objeto!!.telefono)
            txtDireccionCivil.setText(objeto!!.direccion)
            txtFechaNacimiento.setText(objeto!!.fechaNacimiento)
        }
    }

    private fun spinner(){
        val spinner = findViewById<Spinner>(R.id.spnSexoCivil)
        val lista = resources.getStringArray(R.array.spnSexo)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener= object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                if(lista[p2] == "Masculino"){
                    imvCivil.setImageResource(R.drawable.ic_man)
                }else{
                    imvCivil.setImageResource(R.drawable.ic_woman)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            }
    }
    private fun datePickerMetodo(){
        val cal = Calendar.getInstance()
        txtFechaNacimiento.setOnClickListener(View.OnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR,i)
                    selectedDate.set(Calendar.MONTH,i2)
                    selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                    val date = formatDate.format(selectedDate.time)
                    txtFechaNacimiento.setText(date)
                },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        })
    }
    //
    private fun callServicePutCivil() {
        val civilInfo = CivilDataCollectionItem(
            id_civil = txtIdCivil.text.toString().toLong(),
            dni = txtDniCivil.text.toString().toLong(),
            nombre = txtNombreCivil.text.toString(),
            fechaNacimiento = txtFechaNacimiento.text.toString(),
            direccion = txtDireccionCivil.text.toString(),
            telefono = txtTelefonoCivil.text.toString(),
            sexo = spnSexoCivil.selectedItem.toString()
        )
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<CivilDataCollectionItem> = civilService.updateCivil(civilInfo)
        result.enqueue(
            object : Callback<CivilDataCollectionItem>{
                override fun onFailure(call: Call<CivilDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarCivilActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CivilDataCollectionItem>,
                    response: Response<CivilDataCollectionItem>
                ) {
                    if (response.isSuccessful){
                        val updateCivil = response.body()!!
                        Toast.makeText(this@RegistrarCivilActivity,"Actualizado exitosamente :" +updateCivil.nombre,Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@RegistrarCivilActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@RegistrarCivilActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    //
    private fun callServicePostCivil() {
        val civilInfo = CivilDataCollectionItem(
            id_civil = null,
            dni = txtDniCivil.text.toString().toLong(),
            nombre = txtNombreCivil.text.toString(),
            fechaNacimiento = txtFechaNacimiento.text.toString(),
            direccion = txtDireccionCivil.text.toString(),
            telefono = txtTelefonoCivil.text.toString(),
            sexo = spnSexoCivil.selectedItem.toString()
        )
        addPerson(civilInfo){
            if (it?.id_civil != null){
                Toast.makeText(this@RegistrarCivilActivity,"Se ha Registrado correctamente"+it?.nombre, Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this@RegistrarCivilActivity,"Error" + it?.id_civil, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    private fun addPerson(personData:CivilDataCollectionItem, onResult:(CivilDataCollectionItem?)->Unit){
        val personService:CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<CivilDataCollectionItem> = personService.addCivil(personData)
        result.enqueue(object: Callback<CivilDataCollectionItem>{
            override fun onFailure(call: Call<CivilDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<CivilDataCollectionItem>,
                response: Response<CivilDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistrarCivilActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistrarCivilActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}