package hn.edu.ujcv.pdm_2022_i_p3_equipo3


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarVacunaBinding
import kotlinx.android.synthetic.main.content_registrar_civil.*
import kotlinx.android.synthetic.main.content_registrar_vacuna.*
import java.text.SimpleDateFormat
import java.util.*
import android.R
import android.widget.*
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.FabricanteDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.VacunaDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.FabricanteService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.VacunaService
import kotlinx.android.synthetic.main.content_registar_civil_comorbilidad.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarVacunaActivity : AppCompatActivity() {
    private val formatDate = SimpleDateFormat("yyyy-MM-dd",Locale.US)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarVacunaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarVacunaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroVacuna)
        //-----------------------------------------
        callServiceGetFabs()

        //datePickerMetodo()
        datePicker(txtFechaFabricacion)
        datePicker(txtFechaVencimientoVacuna)
        datePicker(txtFechaLlegadaVacuna)
        //
        btnRegistrarVacuna.setOnClickListener { v ->
            if(txtNombreVacuna.text.isNullOrEmpty()){
                Toast.makeText(this@RegistrarVacunaActivity,"Nombre esta vacío",Toast.LENGTH_LONG).show()
            }else if(txtNumeroLoteVacuna.text.isNullOrEmpty()){
                Toast.makeText(this@RegistrarVacunaActivity,"Numero Lote esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePostVacuna()
        }
        btnActualizarVacuna.setOnClickListener { v ->
            if (txtIdVacuna.text.isNullOrEmpty() || txtNombreVacuna.text.isNullOrEmpty() ){
                Toast.makeText(this@RegistrarVacunaActivity,"Datos de la vacuna estan vacíos",Toast.LENGTH_LONG).show()
            }else if(txtNumeroLoteVacuna.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarVacunaActivity, "Numero Lote esta vacío", Toast.LENGTH_LONG).show()
            }else
                callServicePutVacuna()
        }
        obtener()
    }
    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("vacuna") as VacunaDataCollectionItem?
        if (objeto != null) {
            txtIdVacuna.setText(objeto!!.id_vacuna.toString())
            //spnSexoCivil.a ( objeto!!.id_civil.toString())
            txtNombreVacuna.setText(objeto!!.nombre)
            txtNumeroLoteVacuna.setText(objeto!!.numeroLote.toString())
            txtFechaFabricacion.setText(objeto!!.fechaFabricacion)
            txtFechaVencimientoVacuna.setText(objeto!!.fechaVencimiento)
            txtFechaLlegadaVacuna.setText(objeto!!.fechaLlegada)
        }
    }
    private fun datePicker(control:EditText){
        control.setOnClickListener(View.OnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR,i)
                    selectedDate.set(Calendar.MONTH,i2)
                    selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                    val date = formatDate.format(selectedDate.time)
                    control.setText(date)
                },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()
        })
    }
    fun callServiceGetFabs() {
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<List<FabricanteDataCollectionItem>> = fabService.listFabs()
        result.enqueue(
            object : Callback<List<FabricanteDataCollectionItem>> {
                override fun onFailure(call: Call<List<FabricanteDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistrarVacunaActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<FabricanteDataCollectionItem>>,
                    response: Response<List<FabricanteDataCollectionItem>>
                ) {
                    val fabricantes = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        fabricantes.add(ObjetoItem(it.id_fabricante!!,it.laboratorio))
                    }
                    spinnerFabricante(fabricantes)
                }
            }
        )
    }
    fun spinnerFabricante(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(hn.edu.ujcv.pdm_2022_i_p3_equipo3.R.id.spnFabricante)
        val adaptador = ArrayAdapter(this, R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }
    private fun itemSpinner(spinner: Spinner): ObjetoItem{
        return spinner.selectedItem as ObjetoItem
    }
    private fun limpiar() {
        txtIdVacuna.setText("")
        txtNombreVacuna.setText("")
        txtNumeroLoteVacuna.setText("")
        txtFechaFabricacion.setText("")
        txtFechaVencimientoVacuna.setText("")
        txtFechaLlegadaVacuna.setText("")
    }
    //
    private fun callServicePutVacuna() {
        val vacunaInfo = VacunaDataCollectionItem(
            id_vacuna = txtIdVacuna.text.toString().toLong() ,
            id_fabricante = itemSpinner(spnFabricante).id!!,
            nombre = txtNombreVacuna.text.toString(),
            numeroLote = txtNumeroLoteVacuna.text.toString().toLong(),
            fechaFabricacion = txtFechaFabricacion.text.toString(),
            fechaVencimiento = txtFechaVencimientoVacuna.text.toString(),
            fechaLlegada = txtFechaLlegadaVacuna.text.toString()
        )
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<VacunaDataCollectionItem> = vacunaService.updateVacuna(vacunaInfo)
        result.enqueue(
            object : Callback<VacunaDataCollectionItem>{
                override fun onFailure(call: Call<VacunaDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarVacunaActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<VacunaDataCollectionItem>,
                    response: Response<VacunaDataCollectionItem>
                ) {
                    when{
                        response.isSuccessful -> {
                            val updateVacuna = response.body()!!
                            Toast.makeText(this@RegistrarVacunaActivity, "Actualizado Exitosamente: "+ updateVacuna.id_vacuna, Toast.LENGTH_LONG).show()
                            limpiar()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarVacunaActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarVacunaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarVacunaActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //
    private fun callServicePostVacuna(){
        val vacunaInfo = VacunaDataCollectionItem(
            id_vacuna = null ,
            id_fabricante = itemSpinner(spnFabricante).id!!,
            nombre = txtNombreVacuna.text.toString(),
            numeroLote = txtNumeroLoteVacuna.text.toString().toLong(),
            fechaFabricacion = txtFechaFabricacion.text.toString(),
            fechaVencimiento = txtFechaVencimientoVacuna.text.toString(),
            fechaLlegada = txtFechaLlegadaVacuna.text.toString()
        )
        addVacuna(vacunaInfo){
            if (it?.id_vacuna != null){
                Toast.makeText(this@RegistrarVacunaActivity,"Se ha Registrado correctamente", Toast.LENGTH_LONG).show()
                limpiar()
            }else{
                Toast.makeText(this@RegistrarVacunaActivity,"Error " + it?.id_vacuna, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    private fun addVacuna(vacunaData: VacunaDataCollectionItem, onResult:(VacunaDataCollectionItem?)->Unit){
        val vacunaService: VacunaService = RestEngine.buildService().create(
            VacunaService::class.java)
        val result: Call<VacunaDataCollectionItem> = vacunaService.addVacuna(vacunaData)
        result.enqueue(object: Callback<VacunaDataCollectionItem> {
            override fun onFailure(call: Call<VacunaDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<VacunaDataCollectionItem>,
                response: Response<VacunaDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedVacuna = response.body()!!
                    onResult(addedVacuna)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistrarVacunaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistrarVacunaActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}