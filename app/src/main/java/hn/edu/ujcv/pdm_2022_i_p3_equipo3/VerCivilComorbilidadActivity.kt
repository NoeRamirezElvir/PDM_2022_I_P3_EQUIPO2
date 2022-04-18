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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivilComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCivilComorbilidadBinding

class VerCivilComorbilidadActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityVerCivilComorbilidadBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCivilComorbilidad.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerCivilComorbilidadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarVerCivilComorb)

        layoutManager = LinearLayoutManager(this)
        binding.contentCivilComorb.recyclerViewCivilComorbilidad.layoutManager = layoutManager
        actualizarRecyclerView()

        binding.fabCivComorb.setOnClickListener { view ->
            val intent = Intent(this, RegistarCivilComorbilidadActivity::class.java)
            startActivity(intent)
        }
    }
    fun actualizarRecyclerView() {
        adapter = RecyclerAdapterCivilComorbilidad()
        binding.contentCivilComorb.recyclerViewCivilComorbilidad.adapter = adapter
    }


}