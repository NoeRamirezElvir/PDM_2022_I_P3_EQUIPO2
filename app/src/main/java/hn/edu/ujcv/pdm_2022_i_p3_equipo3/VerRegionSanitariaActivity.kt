package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterRegion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerRegionSanitariaBinding

class VerRegionSanitariaActivity : AppCompatActivity() {

    //var listaRegiones:ArrayList<RegionSanitaria>? = ArrayList()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterRegion.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerRegionSanitariaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerRegionSanitariaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerRegion)

        layoutManager = LinearLayoutManager(this)
        binding.contentRegion.recyclerViewRegion.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarRegionSanitariaActivity::class.java)
            startActivity(intent)
        }
    }

    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterRegion()
        binding.contentRegion.recyclerViewRegion.adapter = adapter
    }

    /*override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_ver_region_sanitaria)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }*/
}