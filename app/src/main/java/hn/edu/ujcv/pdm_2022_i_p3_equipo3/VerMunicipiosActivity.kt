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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterMunicipio
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterRegion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerMunicipiosBinding

class VerMunicipiosActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterMunicipio.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerMunicipiosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerMunicipiosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerMunicipio)

        layoutManager = LinearLayoutManager(this)
        binding.contentMunicipio.recyclerMunicipios.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarMunicipioActivity::class.java)
            startActivity(intent)
        }
    }

    private fun actualizarRecyclerView() {
        adapter = RecyclerAdapterMunicipio()
        binding.contentMunicipio.recyclerMunicipios.adapter = adapter
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_ver_municipios)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}