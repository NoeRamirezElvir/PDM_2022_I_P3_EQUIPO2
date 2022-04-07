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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEnfermero
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterVacuna
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerVacunasBinding
import kotlinx.android.synthetic.main.activity_ver_civiles.*

class VerVacunasActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterVacuna.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerVacunasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerVacunasBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVacuna)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentVacunas.recyclerVacunas.layoutManager = layoutManager
        actualizarRecyclerView()
        //---------------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarVacunaActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterVacuna()
        binding.contentVacunas.recyclerVacunas.adapter = adapter
    }

}