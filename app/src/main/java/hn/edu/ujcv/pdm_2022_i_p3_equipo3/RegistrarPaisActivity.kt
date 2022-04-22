package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarPaisBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.PaisesDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.PaisesService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_paises.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarPaisActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegistrarPaisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPaisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroPais)

        recibir()

        btnREgistrarPais.setOnClickListener { v->
            if (txtNombrePaisP.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarPaisActivity,
                    "Nombre del Pais esta vacio", Toast.LENGTH_LONG).show()
            } else
                callServicePostPais()
        }

        btnActualizarPais.setOnClickListener { v->
            if (txtIdPaisP.text.isNullOrEmpty() || txtNombrePaisP.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarPaisActivity,
                    "Datos del pais estan vacios",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePutPais()
        }
    }

    private fun recibir() {
        val intent = intent
        val objeto = intent.getParcelableExtra("paises") as PaisesDataCollectionItem?
        if (objeto != null) {
            txtIdPaisP.setText(objeto.id_pais.toString())
            txtCodigoIsoP.setText(objeto.codigo_iso)
            txtNombrePaisP.setText(objeto.nombre)
            txtCodigoPaisP.setText(objeto.codigo_area)
        }
    }

    private fun callServicePutPais() {
        val codigoIso = txtCodigoIsoP.text.toString().uppercase()
        val paisInfo = PaisesDataCollectionItem(
            id_pais = txtIdPaisP.text.toString().toLong(),
            nombre = txtNombrePaisP.text.toString(),
            codigo_iso = codigoIso,
            codigo_area = txtCodigoPaisP.text.toString()
        )
        val paisService: PaisesService = RestEngine.buildService()
            .create(PaisesService::class.java)
        val result: Call<PaisesDataCollectionItem> = paisService
            .updatePais(paisInfo)
        result.enqueue(
            object : Callback<PaisesDataCollectionItem> {
                override fun onFailure(call: Call<PaisesDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarPaisActivity, "Error",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<PaisesDataCollectionItem>,
                    response: Response<PaisesDataCollectionItem>
                ) {
                    if (response.isSuccessful) {
                        val updatePais = response.body()!!
                        Toast.makeText(this@RegistrarPaisActivity,
                            "Actualizado exitosamente: " + updatePais.nombre,
                            Toast.LENGTH_LONG).show()
                        limpiar()
                    } else if (response.code() == 401) {
                        Toast.makeText(this@RegistrarPaisActivity, "Sesion Expirada",
                            Toast.LENGTH_LONG).show()
                    } else if (response.code()==500) {
                        val errorResponse = Gson().fromJson(
                            response.errorBody()!!.string(),
                            RestApiError::class.java
                        )
                        Toast.makeText(
                            this@RegistrarPaisActivity, errorResponse.errorDetails,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
                        Toast.makeText(this@RegistrarPaisActivity,
                            "Fallo al obtener el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    private fun callServicePostPais() {
        val codigoIso = txtCodigoIsoP.text.toString().uppercase()
        val paisInfo = PaisesDataCollectionItem(
            id_pais = null,
            nombre = txtNombrePaisP.text.toString(),
            codigo_iso = codigoIso,
            codigo_area = txtCodigoPaisP.text.toString()
        )
        addPais(paisInfo) {
            if (it?.id_pais != null) {
                Toast.makeText(this@RegistrarPaisActivity,
                    "Se ha registrado correctamente " + it.nombre, Toast.LENGTH_LONG).show()
                verPaises()
            } else {
                Toast.makeText(this@RegistrarPaisActivity,
                    "Error " + it?.id_pais, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addPais(paisData: PaisesDataCollectionItem,
                        onResult: (PaisesDataCollectionItem?)->Unit) {
        val paisService: PaisesService = RestEngine.buildService()
            .create(PaisesService::class.java)
        val result: Call<PaisesDataCollectionItem> = paisService.addPais(paisData)
        result.enqueue(object : Callback<PaisesDataCollectionItem> {
            override fun onFailure(call: Call<PaisesDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<PaisesDataCollectionItem>,
                response: Response<PaisesDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedPais = response.body()!!
                    onResult(addedPais)
                } else if (response.code()==500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarPaisActivity, errorResponse.errorDetails,
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegistrarPaisActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    private fun verPaises() {
        val intent = Intent(this, VerPaisesActivity::class.java)
        startActivity(intent)
    }

    private fun limpiar() {
        txtIdPaisP.setText("")
        txtNombrePaisP.setText("")
        txtCodigoPaisP.setText("")
        txtCodigoIsoP.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        verPaises()
    }
}

