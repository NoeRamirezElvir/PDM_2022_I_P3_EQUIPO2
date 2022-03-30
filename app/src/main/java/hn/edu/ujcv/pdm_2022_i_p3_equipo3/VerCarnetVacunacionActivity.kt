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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnet
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetVacunacionBinding

class VerCarnetVacunacionActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCarnetVacunacionBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnet.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityVerCarnetVacunacionBinding.inflate(layoutInflater)
     setContentView(binding.root)
        setSupportActionBar(binding.toolbarCarnet)
        //-------------------------------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnet.recyclerCarnet.layoutManager = layoutManager
        actualizarRecyclerView()
        //-----------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarCarnetActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterCarnet()
        binding.contentCarnet.recyclerCarnet.adapter = adapter
    }
}