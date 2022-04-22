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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarEstablecimientoSaludBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EstablecimientoSaludDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EstablecimientoService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.MunicipioService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_establecimiento_salud.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarEstablecimientoActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarEstablecimientoSaludBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarEstablecimientoSaludBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarEstablecimiento)

        recibir()
        callServiceGetMunicipios()

        btnRegistrarEstablecimiento.setOnClickListener { v->
            if (txtNombreEstablecimiento.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarEstablecimientoActivity,
                    "Nombre del Establecimiento esta vacio",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePostEstablecimiento()
        }

        btnActualizarEstablecimiento.setOnClickListener { v->
            if (txtIdEstablecimiento.text.isNullOrEmpty() || txtNombreEstablecimiento.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarEstablecimientoActivity,
                    "Datos del establecimiento estan vacios",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePutEstablecimiento()
        }
    }

    private fun recibir() {
        val intent = intent
        val objeto = intent.getParcelableExtra("establecimiento")
                as EstablecimientoSaludDataCollectionItem?
        if (objeto != null) {
            txtIdEstablecimiento.setText(objeto.id_establecimiento.toString())
            txtNombreEstablecimiento.setText(objeto.nombre)
            txtDireccionEstablecimiento.setText(objeto.direccion)
            txtTelefonoEstablecimiento.setText(objeto.telefono.toString())
        }
    }

    private fun callServicePutEstablecimiento() {
        val establecimientoInfo = EstablecimientoSaludDataCollectionItem(
            id_establecimiento = txtIdEstablecimiento.text.toString().toLong(),
            nombre = txtNombreEstablecimiento.text.toString(),
            direccion = txtDireccionEstablecimiento.text.toString(),
            telefono = txtTelefonoEstablecimiento.text.toString().toLong(),
            id_municipio = itemSpinner(spnMunicipio).id!!
        )
        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<EstablecimientoSaludDataCollectionItem> = establecimientoService
            .updateEstablecimiento(establecimientoInfo)
        result.enqueue(
            object : Callback<EstablecimientoSaludDataCollectionItem> {
                override fun onFailure(call: Call<EstablecimientoSaludDataCollectionItem>,
                                       t: Throwable) {
                    Toast.makeText(this@RegistrarEstablecimientoActivity, "Error",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<EstablecimientoSaludDataCollectionItem>,
                    response: Response<EstablecimientoSaludDataCollectionItem>
                ) {
                    if (response.isSuccessful) {
                        val updateEstablecimiento = response.body()!!
                        Toast.makeText(this@RegistrarEstablecimientoActivity,
                            "Actualizado exitosamente: " + updateEstablecimiento.nombre,
                            Toast.LENGTH_LONG).show()
                        limpiar()
                    } else if (response.code() == 401) {
                        Toast.makeText(this@RegistrarEstablecimientoActivity,
                            "Sesion Expirada", Toast.LENGTH_LONG).show()
                    } else if (response.code() == 500) {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@RegistrarEstablecimientoActivity,
                            errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else {
                        Toast.makeText(this@RegistrarEstablecimientoActivity,
                            "Fallo al obtener el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    private fun callServicePostEstablecimiento() {
        val objetoItem = itemSpinner(spnMunicipio)
        val establecimientoInfo = EstablecimientoSaludDataCollectionItem(
            id_establecimiento = null,
            nombre = txtNombreEstablecimiento.text.toString(),
            direccion = txtDireccionEstablecimiento.text.toString(),
            telefono = txtTelefonoEstablecimiento.text.toString().toLong(),
            id_municipio = objetoItem.id.toString().toLong()
        )
        addEstablecimiento(establecimientoInfo) {
            if (it?.id_establecimiento != null) {
                Toast.makeText(this@RegistrarEstablecimientoActivity,
                    "Se ha registrado correctamente " + it.nombre, Toast.LENGTH_LONG).show()
                verEstablecimientos()
            } else {
                Toast.makeText(this@RegistrarEstablecimientoActivity,
                    "Error " + it?.id_establecimiento, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addEstablecimiento(establecimientoData: EstablecimientoSaludDataCollectionItem,
                                   onResult: (EstablecimientoSaludDataCollectionItem?)->Unit) {
        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<EstablecimientoSaludDataCollectionItem> = establecimientoService
            .addEstablecimiento(establecimientoData)
        result.enqueue(object : Callback<EstablecimientoSaludDataCollectionItem> {
            override fun onFailure(call: Call<EstablecimientoSaludDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<EstablecimientoSaludDataCollectionItem>,
                response: Response<EstablecimientoSaludDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedEstablecimiento = response.body()!!
                    onResult(addedEstablecimiento)
                } else if (response.code()==500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarEstablecimientoActivity,
                        errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegistrarEstablecimientoActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun callServiceGetMunicipios() {
        val municipioService: MunicipioService = RestEngine.buildService()
            .create(MunicipioService::class.java)
        val result: Call<List<MunicipioDataCollectionItem>> = municipioService.listMunicipios()
        result.enqueue(object : Callback<List<MunicipioDataCollectionItem>> {
            override fun onFailure(call: Call<List<MunicipioDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@RegistrarEstablecimientoActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<MunicipioDataCollectionItem>>,
                response: Response<List<MunicipioDataCollectionItem>>
            ) {
                val municipios = arrayListOf<ObjetoItem>()
                for (it in response.body()!!) {
                    municipios.add(ObjetoItem(it.id_municipio, it.nombre))
                }
                llenarSpinnerMunicipio(municipios)
            }
        }
        )
    }

    private fun llenarSpinnerMunicipio(lista: ArrayList<ObjetoItem>?) {
        val spnMunicipio = findViewById<Spinner>(R.id.spnMunicipio)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, lista!!)
        spnMunicipio.adapter = adapter
    }

    private fun itemSpinner(spinner: Spinner): ObjetoItem {
        return spinner.selectedItem as ObjetoItem
    }

    private fun verEstablecimientos() {
        val intent = Intent(this, VerEstablecimientosActivity::class.java)
        startActivity(intent)
    }

    private fun limpiar() {
        txtIdEstablecimiento.setText("")
        txtNombreEstablecimiento.setText("")
        txtDireccionEstablecimiento.setText("")
        txtTelefonoEstablecimiento.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        verEstablecimientos()
    }
}

