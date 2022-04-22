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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistarFabricanteBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.FabricanteDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.PaisesDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.*
import kotlinx.android.synthetic.main.content_registar_fabricante.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistarFabricanteActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistarFabricanteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistarFabricanteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFabricante)
        callServiceGetPaises()
        //
        btnRegistrarFabricante.setOnClickListener { v ->
            if(validar()){
                Toast.makeText(this@RegistarFabricanteActivity,"Datos del Fabricante estan vacíos",Toast.LENGTH_LONG).show()
            }else
                callServicePostFab()
        }
        btnActualizarFabricante.setOnClickListener { v ->
            if (txtIdFabricante.text.isNullOrEmpty() || validar() ){
                Toast.makeText(this@RegistarFabricanteActivity,"Datos del Fabricante estan vacíos",Toast.LENGTH_LONG).show()
            }else
                callServicePutFab()
        }

        obtener()
    }

    private fun validar():Boolean {
        var cond = false
        when{
            txtNombreLab.text.isNullOrEmpty() -> cond = true
            txtNombreContacto.text.isNullOrEmpty() -> cond = true
            txtTelContacto.text.isNullOrEmpty() ->cond = true
        }
        return cond
    }

    //
    private fun callServicePutFab() {
        val fabInfo = FabricanteDataCollectionItem(
            id_fabricante = txtIdFabricante.text.toString().toLong(),
            laboratorio = txtNombreLab.text.toString(),
            nombre_contacto = txtNombreContacto.text.toString(),
            telefono_contacto = txtTelContacto.text.toString().toLong(),
            id_pais = itemSpinner(spnPais).id!!,
        )
        val fabService: FabricanteService = RestEngine.buildService().create(
            FabricanteService::class.java)
        val result: Call<FabricanteDataCollectionItem> = fabService.updateFab(fabInfo)
        result.enqueue(
            object : Callback<FabricanteDataCollectionItem>{
                override fun onFailure(call: Call<FabricanteDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistarFabricanteActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<FabricanteDataCollectionItem>,
                    response: Response<FabricanteDataCollectionItem>
                ) {
                    when{
                        response.isSuccessful -> {
                            val updateCivil = response.body()!!
                            Toast.makeText(this@RegistarFabricanteActivity, "Actualizado Exitosamente: "+ updateCivil.id_fabricante, Toast.LENGTH_LONG).show()
                            limpiar()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@RegistarFabricanteActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code()==500 -> {
                            val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                            Toast.makeText(this@RegistarFabricanteActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@RegistarFabricanteActivity,"Fallo al obtener el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
    //
    private fun callServicePostFab() {
        val fabInfo = FabricanteDataCollectionItem(
            id_fabricante = null,
            laboratorio = txtNombreLab.text.toString(),
            nombre_contacto = txtNombreContacto.text.toString(),
            telefono_contacto = txtTelContacto.text.toString().toLong(),
            id_pais = itemSpinner(spnPais).id!!,
        )
        addFab(fabInfo){
            if (it?.id_fabricante != null){
                Toast.makeText(this@RegistarFabricanteActivity,"Se ha Registrado correctamente", Toast.LENGTH_LONG).show()
                limpiar()
            }else{
                Toast.makeText(this@RegistarFabricanteActivity,"Error" + it?.id_fabricante, Toast.LENGTH_LONG).show()
            }
        }
    }
    //
    fun callServiceGetPaises() {
        val paisService: PaisesService = RestEngine.buildService().create(PaisesService::class.java)
        val result: Call<List<PaisesDataCollectionItem>> = paisService.listPaises()
        result.enqueue(
            object : Callback<List<PaisesDataCollectionItem>> {
                override fun onFailure(call: Call<List<PaisesDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@RegistarFabricanteActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<PaisesDataCollectionItem>>,
                    response: Response<List<PaisesDataCollectionItem>>
                ) {
                    val paises = arrayListOf<ObjetoItem>()
                    for (it in response.body()!!){
                        paises.add(ObjetoItem(it.id_pais!!,it.nombre))
                    }
                    spinnerPais(paises)
                }
            }
        )
    }
    //
    private fun obtener() {
        val intent = intent
        var objeto = intent.getParcelableExtra("fab") as FabricanteDataCollectionItem?
        if (objeto != null) {
            txtIdFabricante.setText(objeto!!.id_fabricante.toString())
            //spnCivil.selec.a ( objeto!!.id_civil.toString())
            txtNombreLab.setText(objeto!!.laboratorio)
            txtNombreContacto.setText(objeto!!.nombre_contacto)
            txtTelContacto.setText(objeto!!.telefono_contacto.toString())
        }
    }
    //
    fun spinnerPais(lista: ArrayList<ObjetoItem>?){
        val spinner = findViewById<Spinner>(R.id.spnPais)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_list_item_1,lista!!)
        spinner.adapter = adaptador
    }
    private fun itemSpinner(spinner: Spinner): ObjetoItem {
        return spinner.selectedItem as ObjetoItem
    }
    private fun limpiar() {
        txtIdFabricante.setText("")
        //spnCivil.selec.a ( objeto!!.id_civil.toString())
        txtNombreLab.setText("")
        txtNombreContacto.setText("")
        txtTelContacto.setText("")
    }
    //
    private fun addFab(fabData: FabricanteDataCollectionItem, onResult:(FabricanteDataCollectionItem?)->Unit){
        val fabService: FabricanteService = RestEngine.buildService().create(FabricanteService::class.java)
        val result: Call<FabricanteDataCollectionItem> = fabService.addFab(fabData)
        result.enqueue(object: Callback<FabricanteDataCollectionItem> {
            override fun onFailure(call: Call<FabricanteDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<FabricanteDataCollectionItem>,
                response: Response<FabricanteDataCollectionItem>
            ) {
                if (response.isSuccessful)
                {
                    val addedFab = response.body()!!
                    onResult(addedFab)
                }
                else if(response.code()==500){
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                    Toast.makeText(this@RegistarFabricanteActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                }
                else
                {
                    Toast.makeText(this@RegistarFabricanteActivity,"Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}