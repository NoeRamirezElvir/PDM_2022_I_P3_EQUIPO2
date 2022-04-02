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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEstablecimiento
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterMunicipio
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerEstablecimientosBinding

class VerEstablecimientosActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterEstablecimiento.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerEstablecimientosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerEstablecimientosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerEstablecimiento)

        layoutManager = LinearLayoutManager(this)
        binding.contentEstablecimientos.recyclerViewEstablecimiento.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarEstablecimientoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarRecyclerView() {
        adapter = RecyclerAdapterEstablecimiento()
        binding.contentEstablecimientos.recyclerViewEstablecimiento.adapter = adapter
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_ver_establecimientos)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}