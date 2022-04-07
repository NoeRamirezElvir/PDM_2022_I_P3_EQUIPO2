package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterPaises
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerPaisesBinding


class VerPaisesActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterPaises.ViewHolder>? = null
    private lateinit var binding:ActivityVerPaisesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerPaisesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerPaises)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentPaises.recyclerPaises.layoutManager = layoutManager
        actualizarRecyclerView()
        //---------------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarPaisActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterPaises()
        binding.contentPaises.recyclerPaises.adapter = adapter
    }
}