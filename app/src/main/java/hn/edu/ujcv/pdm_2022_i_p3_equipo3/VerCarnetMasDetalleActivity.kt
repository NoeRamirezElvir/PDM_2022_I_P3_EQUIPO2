package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetMasDetalleBinding

class VerCarnetMasDetalleActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>? = null
    private lateinit var binding:ActivityVerCarnetMasDetalleBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCarnetMasDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnetMasDetalle.recyclerCarnetDetalleD.layoutManager = layoutManager
        actualizarTablas()

    }
    fun actualizarTablas(){
        adapter = RecyclerAdapterCarnetDetalle()
        binding.contentCarnetMasDetalle.recyclerCarnetDetalleD.adapter = adapter
    }
}