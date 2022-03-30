package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEnfermero
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerEnfermerosBinding

class VerEnfermerosActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterEnfermero.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerEnfermerosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerEnfermerosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarEnfermero)
        //-----------------------------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentEnfermeros.recyclerEnfermeros.layoutManager = layoutManager
        actualizarRecyclerView()
        //fab
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarEnfermeroActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterEnfermero()
        binding.contentEnfermeros.recyclerEnfermeros.adapter = adapter
    }
}