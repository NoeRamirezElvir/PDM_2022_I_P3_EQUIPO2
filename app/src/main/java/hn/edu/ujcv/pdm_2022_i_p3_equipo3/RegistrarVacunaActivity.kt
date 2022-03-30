package hn.edu.ujcv.pdm_2022_i_p3_equipo3


import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarVacunaBinding
import kotlinx.android.synthetic.main.content_registrar_civil.*
import kotlinx.android.synthetic.main.content_registrar_vacuna.*
import java.text.SimpleDateFormat
import java.util.*
import android.R

class RegistrarVacunaActivity : AppCompatActivity() {
    private val formatDate = SimpleDateFormat("dd - MM - yyyy",Locale.US)
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarVacunaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarVacunaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroVacuna)
        //-----------------------------------------
        datePickerMetodo()
    }
    private fun datePickerMetodo(){
        txtFechaFabricacion.setOnClickListener(View.OnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,i)
                selectedDate.set(Calendar.MONTH,i2)
                selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectedDate.time)
                    txtFechaFabricacion.setText(date)

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        })
        txtFechaVencimientoVacuna.setOnClickListener(View.OnClickListener {
            val getDate: Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                R.style.Theme_Holo_Light_Dialog_MinWidth,
                DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->

                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(Calendar.YEAR,i)
                    selectedDate.set(Calendar.MONTH,i2)
                    selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                    val date = formatDate.format(selectedDate.time)
                    txtFechaVencimientoVacuna.setText(date)

                },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        })
    }

}