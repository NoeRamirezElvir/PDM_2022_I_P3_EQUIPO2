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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterDoctor
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerDoctoresBinding

class VerDoctoresActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerDoctoresBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterDoctor.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerDoctoresBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerDoctores)

        layoutManager = LinearLayoutManager(this)
        binding.contentDoctores.recyclerViewDoctores.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabDoctores.setOnClickListener { view ->
            val intent = Intent(this, RegistrarDoctorActivity::class.java)
            startActivity(intent)
        }
    }

    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterDoctor()
        binding.contentDoctores.recyclerViewDoctores.adapter = adapter
    }
}