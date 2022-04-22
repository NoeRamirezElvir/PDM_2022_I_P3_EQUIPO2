package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.R
import android.annotation.SuppressLint
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.ObjetoItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCentroVacunacionBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.*
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.*
import kotlinx.android.synthetic.main.content_registrar_centro_vacunacion.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class RegistrarCentroVacActivity : AppCompatActivity() {

    private val formatTime = SimpleDateFormat("hh:mmaa",Locale.US)
    private lateinit var binding: ActivityRegistrarCentroVacunacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarCentroVacunacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCentroVacunacion)

        recibir()
        llenarSpinners()

        btnRegistrarCentro.setOnClickListener { v->
            if (txtNombreCentro.text.isNullOrEmpty() || txtDireccionCentro.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Datos del Centro de Vacunacion estan vacios",
                    Toast.LENGTH_LONG).show()
            } else if (!validarHorario()) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Horario Invalido!",
                    Toast.LENGTH_LONG).show()
            } else
                callServicePostCentro()
        }

        btnActualizarCentro.setOnClickListener { v->
            if (txtIdCentro.text.isNullOrEmpty() || txtNombreCentro.text.isNullOrEmpty()) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Datos del Centro de Vacunacion estan vacios", Toast.LENGTH_LONG).show()
            } else if (!validarHorario()) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Horario Invalido!",
                    Toast.LENGTH_LONG).show()
            }  else
                callServicePutCentro()
        }
    }

    private fun llenarSpinners() {
        llenarSpinnerTipo()
        llenarSpinnerDias()
        llenarTimePicker()
        callServiceGetEstablecimientos()
    }

    private fun llenarSpinnerTipo() {
        val tipo = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item)
        tipo.addAll("Privado", "Público")
        spnTipo.adapter = tipo
    }

    private fun llenarSpinnerDias() {
        val dia = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item)
        dia.addAll("Lunes", "Martes", "Miercoles", "Jueves",
            "Viernes", "Sabado", "Domingo")
        spnDiaInicio.adapter = dia
        spnDiaFin.adapter = dia


    }

    private fun validarHorario():Boolean {
        when {
            spnDiaInicio.selectedItem.toString() == spnDiaFin.selectedItem.toString() -> return false
            txtHoraInicio.text.toString() == txtHoraFin.text.toString() -> return false
            else -> return true
        }
    }

    @SuppressLint("SetTextI18n")
    private fun llenarTimePicker() {
        txtHoraInicio.setOnClickListener {
            val calendar = Calendar.getInstance()
            val m=true
            val timePicker = TimePickerDialog(this, R.style.Theme_Holo_Light_Dialog_MinWidth,
                TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                    val horaSeleccionada = Calendar.getInstance()
                    horaSeleccionada.set(Calendar.HOUR_OF_DAY,hora)
                    horaSeleccionada.set(Calendar.MINUTE,minuto)
                    val hora = formatTime.format(horaSeleccionada.time)
                    txtHoraInicio.setText(" ${hora.lowercase()}")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), m)
            timePicker.show()
        }

        txtHoraFin.setOnClickListener {
            val calendar = Calendar.getInstance()
            val m=true
            val timePicker = TimePickerDialog(this, R.style.Theme_Holo_Light_Dialog_MinWidth,
                TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                    val horaSeleccionada = Calendar.getInstance()
                    horaSeleccionada.set(Calendar.HOUR_OF_DAY,hora)
                    horaSeleccionada.set(Calendar.MINUTE,minuto)
                    val hora = formatTime.format(horaSeleccionada.time)
                    txtHoraFin.setText(" ${hora.lowercase()}")
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), m)
            timePicker.show()
        }
    }

    private fun recibir() {
        val intent = intent
        val objeto = intent.getParcelableExtra("centro")
                as CentroVacunacionDataCollectionItem?
        if (objeto != null) {
            txtIdCentro.setText(objeto.id_centroVacunacion.toString())
            txtNombreCentro.setText(objeto.nombre)
            txtDireccionCentro.setText(objeto.direccion)
        }
    }

    private fun callServicePutCentro() {
        val horario:String = spnDiaInicio.selectedItem.toString() + "-" +
                spnDiaFin.selectedItem.toString() + " " + txtHoraInicio.text.toString() +
                "-" + txtHoraFin.text.toString()
        val centroInfo = CentroVacunacionDataCollectionItem(
            id_centroVacunacion = txtIdCentro.text.toString().toLong(),
            nombre = txtNombreCentro.text.toString(),
            direccion = txtDireccionCentro.text.toString(),
            tipo = spnTipo.selectedItem.toString(),
            horario = horario,
            id_establecimiento = itemSpinner(spnEstablecimiento).id!!
        )
        val centroService: CentroVacunacionService = RestEngine.buildService()
            .create(CentroVacunacionService::class.java)
        val result: Call<CentroVacunacionDataCollectionItem> = centroService.updateCentro(centroInfo)
        result.enqueue(object : Callback<CentroVacunacionDataCollectionItem> {
            override fun onFailure(call: Call<CentroVacunacionDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CentroVacunacionDataCollectionItem>,
                response: Response<CentroVacunacionDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val updateCentro = response.body()!!
                    Toast.makeText(this@RegistrarCentroVacActivity,
                        "Actualizado exitosamente: " + updateCentro.nombre, Toast.LENGTH_LONG).show()
                    limpiar()
                } else if (response.code() == 401) {
                    Toast.makeText(this@RegistrarCentroVacActivity,
                        "Sesion Expirada", Toast.LENGTH_LONG).show()
                } else if (response.code() == 500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarCentroVacActivity, errorResponse.errorDetails,
                        Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this@RegistrarCentroVacActivity,
                        "Fallo al obtener el item", Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun callServicePostCentro() {
        val horario:String = spnDiaInicio.selectedItem.toString() + "-" +
                spnDiaFin.selectedItem.toString() + " " + txtHoraInicio.text.toString() +
                "-" + txtHoraFin.text.toString()
        val objetoItem = itemSpinner(spnEstablecimiento)
        val centroInfo = CentroVacunacionDataCollectionItem(
            id_centroVacunacion = null,
            nombre = txtNombreCentro.text.toString(),
            direccion = txtDireccionCentro.text.toString(),
            tipo = spnTipo.selectedItem.toString(),
            horario = horario,
            id_establecimiento = objetoItem.id.toString().toLong()
        )
        addCentro(centroInfo) {
            if (it?.id_centroVacunacion != null) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Se ha registrado correctamente " + it.nombre, Toast.LENGTH_LONG).show()
                verCentros()
            } else {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Error " + it?.id_centroVacunacion, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun addCentro(centroData: CentroVacunacionDataCollectionItem,
                          onResult: (CentroVacunacionDataCollectionItem?)->Unit) {
        val centroService: CentroVacunacionService = RestEngine.buildService()
            .create(CentroVacunacionService::class.java)
        val result: Call<CentroVacunacionDataCollectionItem> = centroService
            .addCentro(centroData)
        result.enqueue(object : Callback<CentroVacunacionDataCollectionItem> {
            override fun onFailure(call: Call<CentroVacunacionDataCollectionItem>, t: Throwable) {
                onResult(null)
            }

            override fun onResponse(
                call: Call<CentroVacunacionDataCollectionItem>,
                response: Response<CentroVacunacionDataCollectionItem>
            ) {
                if (response.isSuccessful) {
                    val addedCentro = response.body()!!
                    onResult(addedCentro)
                } else if (response.code()==500) {
                    val errorResponse = Gson().fromJson(response.errorBody()!!.string(),
                        RestApiError::class.java)
                    Toast.makeText(this@RegistrarCentroVacActivity, errorResponse.errorDetails,
                        Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@RegistrarCentroVacActivity,
                        "Fallo al traer el item", Toast.LENGTH_LONG).show()
                }
            }

        })
    }

    fun callServiceGetEstablecimientos() {
        val establecimientoService: EstablecimientoService = RestEngine.buildService()
            .create(EstablecimientoService::class.java)
        val result: Call<List<EstablecimientoSaludDataCollectionItem>> = establecimientoService
            .listEstablecimientos()
        result.enqueue(object : Callback<List<EstablecimientoSaludDataCollectionItem>> {
            override fun onFailure(call: Call<List<EstablecimientoSaludDataCollectionItem>>,
                                   t: Throwable) {
                Toast.makeText(this@RegistrarCentroVacActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<EstablecimientoSaludDataCollectionItem>>,
                response: Response<List<EstablecimientoSaludDataCollectionItem>>
            ) {
                val establecimientos = arrayListOf<ObjetoItem>()
                for (it in response.body()!!) {
                    establecimientos.add(ObjetoItem(it.id_establecimiento, it.nombre))
                }
                llenarSpinnerEstablecimiento(establecimientos)
            }
        }
        )
    }

    private fun llenarSpinnerEstablecimiento(lista: ArrayList<ObjetoItem>?) {
        val spnEstablecimiento = findViewById<Spinner>(hn.edu.ujcv.pdm_2022_i_p3_equipo3.R.id
            .spnEstablecimiento)
        val adapter = ArrayAdapter(this,
            android.R.layout.simple_list_item_1, lista!!)
        spnEstablecimiento.adapter = adapter
    }

    private fun itemSpinner(spinner: Spinner): ObjetoItem {
        return spinner.selectedItem as ObjetoItem
    }

    private fun verCentros() {
        val intent = Intent(this, VerCentroVacunacionActivity::class.java)
        startActivity(intent)
    }

    private fun limpiar() {
        txtIdCentro.setText("")
        txtNombreCentro.setText("")
        txtDireccionCentro.setText("")
        txtHoraInicio.setText("")
        txtHoraFin.setText("")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        verCentros()
    }
}

