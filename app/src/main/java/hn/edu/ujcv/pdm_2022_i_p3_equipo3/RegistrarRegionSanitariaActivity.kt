package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarRegionSanitariaBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RegionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.content_registrar_paises.*
import kotlinx.android.synthetic.main.content_registrar_region_sanitaria.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrarRegionSanitariaActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarRegionSanitariaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarRegionSanitariaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegionSanitaria)

        recibir()

        btnRegistrarRegion.setOnClickListener { v ->
            if (!validarDatos()) {
                Toast.makeText(this@RegistrarRegionSanitariaActivity,
                    "Datos de la region estan vacios", Toast.LENGTH_LONG).show()
            } else
                callServicePostRegion()
        }

        btnActualizarRegion.setOnClickListener { v->
            if (txtIdRegion.text.isNullOrEmpty() || !validarDatos()) {
                Toast.makeText(this@RegistrarRegionSanitariaActivity, "Datos de la region " +
                        "estan vacios", Toast.LENGTH_LONG).show()
            } else
                callServicePutRegion()
        }
    }

    private fun recibir() {
        val intent = intent
        val objeto = intent.getParcelableExtra("region") as RegionDataCollectionItem?
        if (objeto != null) {
            txtIdRegion.setText(objeto.id_region.toString())
            txtDepartamento.setText(objeto.departamento)
            txtJefaturaRegional.setText(objeto.jefatura)
            txtTelefonoRegion.setText(objeto.telefono.toString())
        }
    }

    private fun validarDatos():Boolean {
        when {
            txtDepartamento.text.isNullOrEmpty() -> return false
            txtJefaturaRegional.text.isNullOrEmpty() -> return false
            txtTelefonoRegion.text.isNullOrEmpty() -> return false
            else -> return true
        }
    }

    private fun callServicePutRegion() {
        val regionInfo = RegionDataCollectionItem(
            id_region = txtIdRegion.text.toString().toLong(),
            departamento = txtDepartamento.text.toString(),
            jefatura = txtJefaturaRegional.text.toString(),
            telefono = txtTelefonoRegion.text.toString().toLong()
        )
        val regionService: RegionService = RestEngine.buildService()
            .create(RegionService::class.java)
        val result: Call<RegionDataCollectionItem> = regionService.updateRegionSanitaria(regionInfo)
        result.enqueue(
            object : Callback<RegionDataCollectionItem>{
                override fun onFailure(call: Call<RegionDataCollectionItem>, t: Throwable) {
                    Toast.makeText(this@RegistrarRegionSanitariaActivity, "Error",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<RegionDataCollectionItem>,
                    response: Response<RegionDataCollectionItem>
                ) {
                    if (response.isSuccessful) {
                        val updateRegion = response.body()!!
                        Toast.makeText(this@RegistrarRegionSanitariaActivity,
                            "Actualizado Exitosamente", Toast.LENGTH_LONG).show()
                        limpiar()
                    } else if (response.code()==401) {
                        Toast.makeText(this@RegistrarRegionSanitariaActivity,
                            "Sesion Expirada", Toast.LENGTH_LONG).show()
                    } else if (response.code()==500) {
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@RegistrarRegionSanitariaActivity, errorResponse.errorDetails,
                            Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@RegistrarRegionSanitariaActivity,
                            "Fallo al obtener el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    private fun callServicePostRegion() {
        val regionInfo = RegionDataCollectionItem(
            id_region =  null,
            departamento = txtDepartamento.text.toString(),
            jefatura = txtJefaturaRegional.text.toString(),
            telefono = txtTelefonoRegion.text.toString().toLong()
        )
        addRegion(regionInfo) {
            if (it?.id_region != null) {
                Toast.makeText(this@RegistrarRegionSanitariaActivity, "Se ha registrado " +
                        "correctamente "+it.departamento, Toast.LENGTH_LONG).show()
                verRegiones()
            } else {
                Toast.makeText(this@RegistrarRegionSanitariaActivity, "Error " +
                        it?.id_region, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addRegion(regionData:RegionDataCollectionItem,
                          onResult:(RegionDataCollectionItem?)->Unit) {
        val regionService:RegionService = RestEngine.buildService().create(RegionService::class.java)
        val result: Call<RegionDataCollectionItem> = regionService.addRegionSanitaria(regionData)
        result.enqueue(object: Callback<RegionDataCollectionItem> {
            override fun onFailure(call: Call<RegionDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<RegionDataCollectionItem>,
                response: Response<RegionDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedRegion = response.body()!!
                    onResult(addedRegion)
                } else if (response.code()==500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarRegionSanitariaActivity, errorResponse.errorDetails,
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegistrarRegionSanitariaActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun verRegiones() {
        val intent = Intent(this, VerRegionSanitariaActivity::class.java)
        startActivity(intent)
    }

    private fun limpiar() {
        txtIdRegion.setText("")
        txtDepartamento.setText("")
        txtJefaturaRegional.setText("")
        txtTelefonoRegion.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        verRegiones()
    }
}
