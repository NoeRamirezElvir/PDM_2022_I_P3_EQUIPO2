package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarComorbilidadBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.ComorbilidadService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_civil.*
import kotlinx.android.synthetic.main.content_registrar_comorbilidad.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarComorbilidad : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarComorbilidadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarComorbilidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarComorbilidad)

        btnRegistrarComorbilidad.setOnClickListener { v ->
            if (txtNombreComorbilidad.text.isNullOrEmpty()){
                Toast.makeText(this@RegistrarComorbilidad,"Nombre de Comorbilidad esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePostComorb()
        }
        btnActualizarComorbilidad.setOnClickListener { v->
            if (txtIdComorbilidad.text.isNullOrEmpty() || txtNombreComorbilidad.text.isNullOrEmpty() ){
                Toast.makeText(this@RegistrarComorbilidad,"Datos de la comorbilidad estan vacíos",Toast.LENGTH_LONG).show()
            }else
                callServicePutComorb()
        }
        obtener()
    }
    //
    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("comorb") as ComorbilidadDataCollectionItem?
        if (objeto != null) {
            txtIdComorbilidad.setText(objeto!!.id_comorbilidad.toString())
            txtNombreComorbilidad.setText(objeto!!.nombre)
            txtTipoComorbilidad.setText(objeto!!.tipo)
        }
    }
    //
    private fun callServicePutComorb() {
        val comorbInfo = ComorbilidadDataCollectionItem(
            id_comorbilidad = txtIdComorbilidad.text.toString().toLong(),
            nombre = txtNombreComorbilidad.text.toString(),
            tipo = txtTipoComorbilidad.text.toString()
        )
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<ComorbilidadDataCollectionItem> = comorbService.updateComorb(comorbInfo)
        result.enqueue(
            object : Callback<ComorbilidadDataCollectionItem>{
                override fun onFailure(call: Call<ComorbilidadDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarComorbilidad,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ComorbilidadDataCollectionItem>,
                    response: Response<ComorbilidadDataCollectionItem>
                ) {
                    when{
                        response.isSuccessful -> {
                            val updateCivil = response.body()!!
                            Toast.makeText(this@RegistrarComorbilidad, "Actualizado Exitosamente: "+ updateCivil.id_comorbilidad, Toast.LENGTH_LONG).show()
                            limpiar()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarComorbilidad,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarComorbilidad,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarComorbilidad,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //
    private fun callServicePostComorb() {
        val comorbInfo = ComorbilidadDataCollectionItem(
            id_comorbilidad = null ,
            nombre = txtNombreComorbilidad.text.toString(),
            tipo = txtTipoComorbilidad.text.toString()
        )
        addComorb(comorbInfo){
            if (it?.id_comorbilidad != null){
                Toast.makeText(this@RegistrarComorbilidad,"Registro Exítoso", Toast.LENGTH_LONG).show()
                limpiar()
            }else{
                Toast.makeText(this@RegistrarComorbilidad,"Error" + it?.id_comorbilidad, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun limpiar() {
        txtIdComorbilidad.setText("")
        txtNombreComorbilidad.setText("")
        txtTipoComorbilidad.setText("")
    }

    //
    private fun addComorb(comorbData: ComorbilidadDataCollectionItem, onResult:(ComorbilidadDataCollectionItem?)->Unit){
        val comorbService: ComorbilidadService = RestEngine.buildService().create(ComorbilidadService::class.java)
        val result: Call<ComorbilidadDataCollectionItem> = comorbService.addComorb(comorbData)
        result.enqueue(object: Callback<ComorbilidadDataCollectionItem> {
            override fun onFailure(call: Call<ComorbilidadDataCollectionItem>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse(
                call: Call<ComorbilidadDataCollectionItem>,
                response: Response<ComorbilidadDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedPerson = response.body()!!
                    onResult(addedPerson)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistrarComorbilidad,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistrarComorbilidad,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}