package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterEmpleado
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerEmpleadoBinding

class VerEmpleadosActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterEmpleado.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerEmpleadoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerEmpleadoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarEmpleado)
        //-----------------------------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentEmpleado.recyclerEmpleado.layoutManager = layoutManager
        actualizarRecyclerView()
        //fab
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarEmpleadosActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterEmpleado()
        binding.contentEmpleado.recyclerEmpleado.adapter = adapter
    }
}