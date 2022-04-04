package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterRegion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerComorbilidadesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerRegionSanitariaBinding

class VerComorbilidadesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerComorbilidadesBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterComorbilidad.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerComorbilidadesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerComorbilidades)

        layoutManager = LinearLayoutManager(this)
        binding.contentComorbilidades.recyclerViewComorbilidad.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabComorbilidad.setOnClickListener { view ->
            val intent = Intent(this, RegistrarComorbilidad::class.java)
            startActivity(intent)
        }

    }
    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterComorbilidad()
        binding.contentComorbilidades.recyclerViewComorbilidad.adapter = adapter
    }

}