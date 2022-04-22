package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistarCivilComorbilidadBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.ComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registar_civil_comorbilidad.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistarCivilComorbilidadActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistarCivilComorbilidadBinding
    val itemPacienteComorb : ArrayList<ObjetoItem>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistarCivilComorbilidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        callServiceGetCiviles()
        //----
        callServiceGetComorbs()

        btnRegistrarCivilComorb.setOnClickListener { v ->
            if(txtEstado.text.isNullOrEmpty()){
                Toast.makeText(this@RegistarCivilComorbilidadActivity,"Estado esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePostCivComorb()
        }
        btnActualizarCivilComorb.setOnClickListener { v ->
            if (txtIdCivilComorb.text.isNullOrEmpty() || txtEstado.text.isNullOrEmpty() ){
                Toast.makeText(this@RegistarCivilComorbilidadActivity,"Datos del Paciente estan vacíos",Toast.LENGTH_LONG).show()
            }else
                callServicePutCivComorb()
        }
        obtener()
    }
    //
    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("civilcomorb") as CivilComorbilidadDataCollectionItem?
        if (objeto != null) {
            txtIdCivilComorb.setText(objeto!!.id_civilComorbilidad.toString())
            //spnCivil.selec.a ( objeto!!.id_civil.toString())
            txtEstado.setText(objeto!!.estado)
            txtObservacion.setText(objeto!!.observacion)
        }
    }
    //
    fun callServiceGetCiviles() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<List<CivilDataCollectionItem>> = civilService.listCiviles()
        result.enqueue(
            object : Callback<List<CivilDataCollectionItem>> {
                override fun onFailure(call: Call<List<CivilDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistarCivilComorbilidadActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CivilDataCollectionItem>>,
                    response: Response<List<CivilDataCollectionItem>>
                ) {
                    val pacientes = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        pacientes.add(ObjetoItem(it.id_civil!!,it.nombre))
                    }
                    spinnerPaciente(pacientes)
                }
            }
        )
    }
    //
    fun callServiceGetComorbs() {
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<List<ComorbilidadDataCollectionItem>> = comorbService.listComorbs()
        result.enqueue(
            object : Callback<List<ComorbilidadDataCollectionItem>> {
                override fun onFailure(call: Call<List<ComorbilidadDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistarCivilComorbilidadActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<ComorbilidadDataCollectionItem>>,
                    response: Response<List<ComorbilidadDataCollectionItem>>
                ) {
                    val comorbilidades = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        comorbilidades.add(ObjetoItem(it.id_comorbilidad!!,it.nombre))
                    }
                    spinnerComorbilidad(comorbilidades)
                }
            }
        )
    }
    //
    private fun itemSpinner(spinner: Spinner): ObjetoItem{
        return spinner.selectedItem as ObjetoItem
    }

    //
    private fun callServicePutCivComorb() {
        val civilComorbInfo = CivilComorbilidadDataCollectionItem(
            id_civilComorbilidad = txtIdCivilComorb.text.toString().toLong() ,
            id_civil = itemSpinner(spnCivil).id!!,
            id_comorbilidad = itemSpinner(spnComorbilidad).id!!,
            estado = txtEstado.text.toString(),
            observacion =txtObservacion.text.toString()
        )
        val civilComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<CivilComorbilidadDataCollectionItem> = civilComorbService.updateCivComorb(civilComorbInfo)
        result.enqueue(
            object : Callback<CivilComorbilidadDataCollectionItem>{
                override fun onFailure(call: Call<CivilComorbilidadDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistarCivilComorbilidadActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CivilComorbilidadDataCollectionItem>,
                    response: Response<CivilComorbilidadDataCollectionItem>
                ) {
                    when{
                        response.isSuccessful -> {
                            val updateCivil = response.body()!!
                            Toast.makeText(this@RegistarCivilComorbilidadActivity, "Actualizado Exitosamente: "+ updateCivil.id_civilComorbilidad, Toast.LENGTH_LONG).show()
                            limpiar()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistarCivilComorbilidadActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistarCivilComorbilidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistarCivilComorbilidadActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //
    private fun callServicePostCivComorb(){
        val civilComorbInfo = CivilComorbilidadDataCollectionItem(
            id_civilComorbilidad = null ,
            id_civil = itemSpinner(spnCivil).id!!,
            id_comorbilidad = itemSpinner(spnComorbilidad).id!!,
            estado = txtEstado.text.toString(),
            observacion =txtObservacion.text.toString()
        )
        addCivComorb(civilComorbInfo){
            if (it?.id_civilComorbilidad != null){
                Toast.makeText(this@RegistarCivilComorbilidadActivity,"Se ha Registrado correctamente", Toast.LENGTH_LONG).show()
                limpiar()
            }else{
                Toast.makeText(this@RegistarCivilComorbilidadActivity,"Error" + it?.id_civil, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun limpiar() {
        txtIdCivilComorb.setText("")
        txtEstado.setText("")
        txtObservacion.setText("")
    }
    //
    fun spinnerPaciente(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(R.id.spnCivil)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }
    fun spinnerComorbilidad(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(R.id.spnComorbilidad)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }

    //
    private fun addCivComorb(civComorbData: CivilComorbilidadDataCollectionItem, onResult:(CivilComorbilidadDataCollectionItem?)->Unit){
        val civComorbService: CivilComorbilidadService = RestEngine.buildService().create(CivilComorbilidadService::class.java)
        val result: Call<CivilComorbilidadDataCollectionItem> = civComorbService.addCivComorb(civComorbData)
        result.enqueue(object: Callback<CivilComorbilidadDataCollectionItem> {
            override fun onFailure(call: Call<CivilComorbilidadDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<CivilComorbilidadDataCollectionItem>,
                response: Response<CivilComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistarCivilComorbilidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistarCivilComorbilidadActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}