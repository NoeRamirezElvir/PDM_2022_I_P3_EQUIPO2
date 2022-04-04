package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterUnidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerUnidadesBinding

class VerUnidadesActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterUnidad.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerUnidadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerUnidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerUnidades)

        layoutManager = LinearLayoutManager(this)
        binding.contentUnidades.recyclerUnidades.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabUnidades.setOnClickListener { view ->
            val intent = Intent(this, RegistrarUnidadActivity::class.java)
            startActivity(intent)
        }
    }

    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterUnidad()
        binding.contentUnidades.recyclerUnidades.adapter = adapter
    }

}