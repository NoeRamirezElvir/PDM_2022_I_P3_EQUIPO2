package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterPaises
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarPaisBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerPaisesBinding

class RegistrarPaisActivity : AppCompatActivity() {
    private lateinit var binding:ActivityRegistrarPaisBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarPaisBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroPais)
    }

}