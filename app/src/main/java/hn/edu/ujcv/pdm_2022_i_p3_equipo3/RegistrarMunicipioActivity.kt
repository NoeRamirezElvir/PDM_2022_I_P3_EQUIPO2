package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarMunicipioBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.MunicipioService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RegionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_municipio.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarMunicipioActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarMunicipioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarMunicipioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarMunicipio)

        recibir()
        callServiceGetRegiones()

        btnRegistrarMunicipio.setOnClickListener { v->
            if (txtNombreMunicipio.text.isNullOrEmpty()) {
                Toast.makeText(this, "Nombre del Municipio esta vacio",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePostMunicipio()
        }

        btnActualizarMunicipio.setOnClickListener { v->
            if (txtIDMunicipio.text.isNullOrEmpty() || txtNombreMunicipio.text.isNullOrEmpty()) {
                Toast.makeText(this, "Datos del municipio estan vacios",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePutMunicipio()
        }

    }

    private fun recibir() {
        val intent = intent
        val objeto = intent.getParcelableExtra("municipio")
                as MunicipioDataCollectionItem?
        if (objeto != null) {
            txtIDMunicipio.setText(objeto.id_municipio.toString())
            txtNombreMunicipio.setText(objeto.nombre)
        }
    }

    private fun callServicePutMunicipio() {
        val municipioInfo = MunicipioDataCollectionItem(
            id_municipio = txtIDMunicipio.text.toString().toLong(),
            nombre = txtNombreMunicipio.text.toString(),
            id_region = itemSpinner(spnRegion).id!!
        )
        val municipioService: MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<MunicipioDataCollectionItem> = municipioService
            .updateMunicipio(municipioInfo)
        result.enqueue(
            object : Callback<MunicipioDataCollectionItem> {
                override fun onFailure(call: Call<MunicipioDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarMunicipioActivity, "Error",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<MunicipioDataCollectionItem>,
                    response: Response<MunicipioDataCollectionItem>
                ) {
                    if (response.isSuccessful) {
                        val updateMunicipio = response.body()!!
                        Toast.makeText(this@RegistrarMunicipioActivity,
                            "Actualizado exitosamente: " + updateMunicipio.nombre,
                            Toast.LENGTH_LONG).show()
                        limpiar()
                    } else if (response.code() == 401) {
                        Toast.makeText(this@RegistrarMunicipioActivity, "Sesion Expirada",
                            Toast.LENGTH_LONG).show()
                    } else if (response.code() == 500) {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@RegistrarMunicipioActivity,
                            errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this@RegistrarMunicipioActivity,
                            "Fallo al obtener el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    private fun callServicePostMunicipio() {
        val objetoItem = itemSpinner(spnRegion)
        val municipioInfo = MunicipioDataCollectionItem(
            id_municipio = null,
            nombre = txtNombreMunicipio.text.toString(),
            id_region = objetoItem.id.toString().toLong()
        )
        addMunicipio(municipioInfo) {
            if (it?.id_municipio != null) {
                Toast.makeText(this@RegistrarMunicipioActivity,
                    "Se ha registrado correctamente " + it.nombre, Toast.LENGTH_LONG).show()
                verMunicipios()
            } else {
                Toast.makeText(this@RegistrarMunicipioActivity,
                    "Error " + it?.id_municipio, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addMunicipio(municipioData:MunicipioDataCollectionItem,
                             onResult: (MunicipioDataCollectionItem?)->Unit) {
        val municipioService:MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<MunicipioDataCollectionItem> = municipioService.addMunicipio(municipioData)
        result.enqueue(object : Callback<MunicipioDataCollectionItem> {
            override fun onFailure(call: Call<MunicipioDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<MunicipioDataCollectionItem>,
                response: Response<MunicipioDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedMunicipio = response.body()!!
                    onResult(addedMunicipio)
                } else if (response.code()==500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarMunicipioActivity, errorResponse.errorDetails,
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegistrarMunicipioActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun callServiceGetRegiones() {
        val regionService: RegionService = RestEngine.buildService().create(RegionService::class.java)
        val result: Call<List<RegionDataCollectionItem>> = regionService.listRegiones()
        result.enqueue(object : Callback<List<RegionDataCollectionItem>> {
            override fun onFailure(call: Call<List<RegionDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarMunicipioActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<RegionDataCollectionItem>>,
                response: Response<List<RegionDataCollectionItem>>
            ) {
                val regiones = arrayListOf<ObjetoItem>()
                for (it in response.body()!!) {
                    regiones.add(ObjetoItem(it.id_region, it.departamento))
                }
                llenarSpinnerRegion(regiones)
            }
        }
        )
    }

    private fun llenarSpinnerRegion(lista: ArrayList<ObjetoItem>?) {
        val spnRegion = findViewById<Spinner>(R.id.spnRegion)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, lista!!)
        spnRegion.adapter = adapter
    }

    private fun itemSpinner(spinner: Spinner): ObjetoItem {
        return spinner.selectedItem as ObjetoItem
    }

    private fun verMunicipios() {
        val intent = Intent(this, VerMunicipiosActivity::class.java)
        startActivity(intent)
    }

    private fun limpiar() {
        txtIDMunicipio.setText("")
        txtNombreMunicipio.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        verMunicipios()
    }
}
