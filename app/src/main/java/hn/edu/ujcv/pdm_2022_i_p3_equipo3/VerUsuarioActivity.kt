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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterUsuario
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterVacuna
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerUsuarioBinding

class VerUsuarioActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterUsuario.ViewHolder>? = null
    private lateinit var appBarConfiguration: AppBarConfiguration
private lateinit var binding: ActivityVerUsuarioBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
     binding = ActivityVerUsuarioBinding.inflate(layoutInflater)
     setContentView(binding.root)
        setSupportActionBar(binding.toolbarUsuarios)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentUsuarios.recyclerUsuarios.layoutManager = layoutManager
        actualizarRecyclerView()
        //---------------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarUsuarioActivity::class.java)
            startActivity(intent)
        }

    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterUsuario()
        binding.contentUsuarios.recyclerUsuarios.adapter = adapter
    }

}