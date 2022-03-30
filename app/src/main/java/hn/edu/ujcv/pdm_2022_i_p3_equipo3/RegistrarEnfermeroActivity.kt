package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarEnfermeroBinding

class RegistrarEnfermeroActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityRegistrarEnfermeroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarEnfermeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroEnfermeros)
        //---------------------------

    }

}