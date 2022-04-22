package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarUnidadBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.UnidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.VacunaDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.*
import kotlinx.android.synthetic.main.content_registrar_unidad.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList

class RegistrarUnidadActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarUnidadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarUnidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarUnidad)

        callServiceGetCentros()
        callServiceGetVacunas()
        //
        btnRegistrarUnidad.setOnClickListener { v ->
            if(txtTipoUnidad.text.isNullOrEmpty()){
                Toast.makeText(this@RegistrarUnidadActivity,"Tipo esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePostUnidad()
        }
        //|| txtTipoUnidad.text.isNullOrEmpty()
        btnActualizarUnidad.setOnClickListener { v ->
            if (txtIdUnidad.text.isNullOrEmpty() ){
                Toast.makeText(this@RegistrarUnidadActivity,"Id de la vacuna esta vacío",Toast.LENGTH_LONG).show()
            }else
                callServicePutUnidad()
        }

        obtener()
    }
    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("unidad") as UnidadDataCollectionItem?
        if (objeto != null) {
            txtIdUnidad.setText(objeto!!.id_unidad.toString())
            txtTipoUnidad.setText(objeto!!.tipo)
        }
    }
    fun callServiceGetCentros() {
        val centroService: CentroVacunacionService = RestEngine.buildService().create(CentroVacunacionService::class.java)
        val result: Call<List<CentroVacunacionDataCollectionItem>> = centroService.listCentrosVacunacion()
        result.enqueue(
            object : Callback<List<CentroVacunacionDataCollectionItem>> {
                override fun onFailure(call: Call<List<CentroVacunacionDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistrarUnidadActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CentroVacunacionDataCollectionItem>>,
                    response: Response<List<CentroVacunacionDataCollectionItem>>
                ) {
                    val centros = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        centros.add(ObjetoItem(it.id_centroVacunacion!!,it.nombre))
                    }
                    spinnerCentros(centros)
                }
            }
        )
    }
    fun spinnerCentros(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(hn.edu.ujcv.pdm_2022_i_p3_equipo3.R.id.spnCentroVU)
        val adaptador = ArrayAdapter(this, R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }
    //
    fun callServiceGetVacunas() {
        val vacunaService: VacunaService = RestEngine.buildService().create(VacunaService::class.java)
        val result: Call<List<VacunaDataCollectionItem>> = vacunaService.listVacunas()
        result.enqueue(
            object : Callback<List<VacunaDataCollectionItem>> {
                override fun onFailure(call: Call<List<VacunaDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistrarUnidadActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<VacunaDataCollectionItem>>,
                    response: Response<List<VacunaDataCollectionItem>>
                ) {
                    val vacunas = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        vacunas.add(ObjetoItem(it.id_vacuna!!,it.nombre))
                    }
                    spinnerVacunas(vacunas)
                }
            }
        )
    }
    fun spinnerVacunas(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(hn.edu.ujcv.pdm_2022_i_p3_equipo3.R.id.spnVacunaUnidad)
        val adaptador = ArrayAdapter(this, R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }

    private fun itemSpinner(spinner: Spinner): ObjetoItem{
        return spinner.selectedItem as ObjetoItem
    }
    private fun limpiar() {
        txtIdUnidad.setText("")
        txtTipoUnidad.setText("")
    }
    //
    private fun callServicePutUnidad() {
        val unidadInfo = UnidadDataCollectionItem(
            id_unidad = txtIdUnidad.text.toString().toLong() ,
            id_centro = itemSpinner(spnCentroVU).id!!,
            id_vacuna_suministrar = itemSpinner(spnVacunaUnidad).id!!,
            tipo = txtTipoUnidad.text.toString()
        )
        val unidadService: UnidadService = RestEngine.buildService().create(UnidadService::class.java)
        val result: Call<UnidadDataCollectionItem> = unidadService.updateUnidad(unidadInfo)
        result.enqueue(
            object : Callback<UnidadDataCollectionItem>{
                override fun onFailure(call: Call<UnidadDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarUnidadActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<UnidadDataCollectionItem>,
                    response: Response<UnidadDataCollectionItem>
                ) {
                    when{
                        response.isSuccessful -> {
                            val updateUnidad= response.body()!!
                            Toast.makeText(this@RegistrarUnidadActivity, "Actualizado Exitosamente: "+ updateUnidad.id_unidad, Toast.LENGTH_LONG).show()
                            limpiar()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistrarUnidadActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistrarUnidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistrarUnidadActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //
    private fun callServicePostUnidad(){
        val unidadInfo = UnidadDataCollectionItem(
            id_unidad = null ,
            id_centro = itemSpinner(spnCentroVU).id!!,
            id_vacuna_suministrar = itemSpinner(spnVacunaUnidad).id!!,
            tipo = txtTipoUnidad.text.toString()
        )
        addUnidad(unidadInfo){
            if (it?.id_unidad != null){
                Toast.makeText(this@RegistrarUnidadActivity,"Se ha Registrado correctamente", Toast.LENGTH_LONG).show()
                limpiar()
            }else{
                Toast.makeText(this@RegistrarUnidadActivity,"Error " + it?.id_unidad, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    private fun addUnidad(unidadData: UnidadDataCollectionItem, onResult:(UnidadDataCollectionItem?)->Unit){
        val unidadervice: UnidadService = RestEngine.buildService().create(
            UnidadService::class.java)
        val result: Call<UnidadDataCollectionItem> = unidadervice.addUnidad(unidadData)
        result.enqueue(object: Callback<UnidadDataCollectionItem> {
            override fun onFailure(call: Call<UnidadDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<UnidadDataCollectionItem>,
                response: Response<UnidadDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedUnidad= response.body()!!
                    onResult(addedUnidad)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistrarUnidadActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistrarUnidadActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}