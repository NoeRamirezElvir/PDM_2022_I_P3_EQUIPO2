package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCarnetDetalleBinding

class RegistrarCarnetDetalleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrarCarnetDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCarnetDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCarnetDetalle)
        binding.contentCarnetDetalle.btnVerListaDetalles.setOnClickListener {
            val intent = Intent(this,VerCarnetDetalleActivity::class.java)
            startActivity(intent)
        }
    }
}