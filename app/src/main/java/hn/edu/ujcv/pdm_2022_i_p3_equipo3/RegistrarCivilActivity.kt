package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCivilBinding
import kotlinx.android.synthetic.main.content_registrar_civil.*
import java.text.SimpleDateFormat
import java.util.*

class RegistrarCivilActivity : AppCompatActivity() {
    private val formatDate = SimpleDateFormat("dd - MM - yyyy",Locale.US)
    private lateinit var binding: ActivityRegistrarCivilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCivilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCiviles)
        //-----------------------------------
        spinner()
        datePickerMetodo()
    }
    private fun spinner(){
        val spinner = findViewById<Spinner>(R.id.spnSexoCivil)
        val lista = resources.getStringArray(R.array.spnSexo)
        val adaptador = ArrayAdapter(this,android.R.layout.simple_spinner_item,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener= object:
            AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                p2: Int,
                p3: Long
            ) {
                if(lista[p2] == "Masculino"){
                    imvCivil.setImageResource(R.drawable.ic_man)
                }else{
                    imvCivil.setImageResource(R.drawable.ic_woman)
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

            }
    }
    private fun datePickerMetodo(){
        txtFechaNacimiento.setOnClickListener(View.OnClickListener {
            val getDate:Calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,DatePickerDialog.OnDateSetListener { _, i, i2, i3 ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR,i)
                selectedDate.set(Calendar.MONTH,i2)
                selectedDate.set(Calendar.DAY_OF_MONTH,i3)
                val date = formatDate.format(selectedDate.time)
                txtFechaNacimiento.setText(date)

            },getDate.get(Calendar.YEAR),getDate.get(Calendar.MONTH),getDate.get(Calendar.DAY_OF_MONTH))
            datePicker.show()

        })
    }
    //

}