package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCivilesBinding

class VerCivilesActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCivil.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCivilesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCivilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCiviles)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentCiviles.recyclerCiviles.layoutManager = layoutManager
        actualizarRecyclerView()
        //-----------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarCivilActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterCivil()
        binding.contentCiviles.recyclerCiviles.adapter = adapter
    }
}