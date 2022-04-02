package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.R
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCentroVacunacionBinding
import kotlinx.android.synthetic.main.content_registrar_centro_vacunacion.*
import kotlinx.android.synthetic.main.content_registrar_vacuna.*
import java.text.SimpleDateFormat
import java.util.*

class RegistrarCentroVacActivity : AppCompatActivity() {

    private val formatTime = SimpleDateFormat("hh:mmaa",Locale.US)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarCentroVacunacionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrarCentroVacunacionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarCentroVacunacion)
        llenarSpinnerTipo()
        llenarSpinnerDias()
        llenarTimePicker()
    }

    private fun llenarSpinnerTipo() {
        var tipo = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item)
        tipo.addAll("Privado", "Público")
        spnTipo.adapter = tipo
    }

    private fun llenarSpinnerDias() {
        var dia = ArrayAdapter<String>(this,
            R.layout.simple_spinner_dropdown_item)
        dia.addAll("Lunes", "Martes", "Miercoles", "Jueves",
        "Viernes", "Sabado", "Domingo")
        spnDiaInicio.adapter = dia
        spnDiaFin.adapter = dia
    }

    @SuppressLint("SetTextI18n")
    private fun llenarTimePicker() {
        txtHoraInicio.setOnClickListener {
        val calendar = Calendar.getInstance()
        var m:Boolean=true
        val timePicker = TimePickerDialog(this, R.style.Theme_Holo_Light_Dialog_MinWidth,
            TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                val horaSeleccionada = Calendar.getInstance()
                horaSeleccionada.set(Calendar.HOUR_OF_DAY,hora)
                horaSeleccionada.set(Calendar.MINUTE,minuto)
                val hora = formatTime.format(horaSeleccionada.time)
                txtHoraInicio.setText(" $hora")
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), m)
            timePicker.show()
        }

        txtHoraFin.setOnClickListener {
         val calendar = Calendar.getInstance()
         var m:Boolean=true
         val timePicker = TimePickerDialog(this, R.style.Theme_Holo_Light_Dialog_MinWidth,
             TimePickerDialog.OnTimeSetListener { timePicker, hora, minuto ->
                 val horaSeleccionada = Calendar.getInstance()
                 horaSeleccionada.set(Calendar.HOUR_OF_DAY,hora)
                 horaSeleccionada.set(Calendar.MINUTE,minuto)
                 val hora = formatTime.format(horaSeleccionada.time)
                 txtHoraFin.setText(" $hora")
             }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), m)
         timePicker.show()
        }
    }


}