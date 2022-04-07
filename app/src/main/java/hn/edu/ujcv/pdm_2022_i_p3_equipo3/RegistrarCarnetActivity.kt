package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityRegistrarCarnetBinding

class RegistrarCarnetActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>? = null
    private lateinit var binding: ActivityRegistrarCarnetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrarCarnetBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarRegistroCarnet)
        //---------------------------
        layoutManager = LinearLayoutManager(this)
        binding.contentDetalles.recyclerDetallesC.layoutManager = layoutManager
        actualizarRecyclerView()
        ///
        binding.contentDetalles.btnAgregarDetalle.setOnClickListener {
            val intent = Intent(this,RegistrarCarnetDetalleActivity::class.java)
            startActivity(intent)
        }

    }
    fun actualizarRecyclerView(){
        adapter = RecyclerAdapterCarnetDetalle()
        binding.contentDetalles.recyclerDetallesC.adapter = adapter
    }

}