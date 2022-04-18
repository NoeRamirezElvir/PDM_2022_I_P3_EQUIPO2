package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCargo
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCargosBinding

class VerCargosActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCargosBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCargo.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerCargosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerCargos)

        layoutManager = LinearLayoutManager(this)
        binding.contentCargos.recyclerViewCargos.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabCargos.setOnClickListener { view ->
            val intent = Intent(this, RegistrarCargoActivity::class.java)
            startActivity(intent)
        }
    }

    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterCargo()
        binding.contentCargos.recyclerViewCargos.adapter = adapter
    }
}