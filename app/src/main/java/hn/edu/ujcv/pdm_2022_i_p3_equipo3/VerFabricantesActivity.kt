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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterFabricante
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerFabricantesBinding

class VerFabricantesActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerFabricantesBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterFabricante.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerFabricantesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerFabricantes)

        layoutManager = LinearLayoutManager(this)
        binding.contentFabricantes.recyclerViewFabricantes.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabFabricantes.setOnClickListener { view ->
            val intent = Intent(this, RegistarFabricanteActivity::class.java)
            startActivity(intent)
        }
    }

    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterFabricante()
        binding.contentFabricantes.recyclerViewFabricantes.adapter = adapter
    }
}