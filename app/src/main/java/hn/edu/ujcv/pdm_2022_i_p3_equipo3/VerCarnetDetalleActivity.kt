package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetDetalleBinding


class VerCarnetDetalleActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>? = null
    private lateinit var binding: ActivityVerCarnetDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCarnetDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCarnetDetalle)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnetDetalle.recyclerCarnetDetalleD.layoutManager = layoutManager
        actualizarRecyclerView()
        //

    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterCarnetDetalle()
        binding.contentCarnetDetalle.recyclerCarnetDetalleD.adapter = adapter
    }
}

