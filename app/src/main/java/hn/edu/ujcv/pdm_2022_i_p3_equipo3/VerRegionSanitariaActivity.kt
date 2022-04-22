package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterRegion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerRegionSanitariaBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.EmpleadosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RegionService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_empleado.*
import kotlinx.android.synthetic.main.activity_ver_region_sanitaria.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerRegionSanitariaActivity : AppCompatActivity() {

    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterRegion.ViewHolder>? = null
    private lateinit var binding: ActivityVerRegionSanitariaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerRegionSanitariaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerRegion)

        layoutManager = LinearLayoutManager(this)
        binding.contentRegion.recyclerViewRegion.layoutManager = layoutManager
        callServiceGetRegiones()

        binding.fab.setOnClickListener { view ->
            val intent = Intent(this, RegistrarRegionSanitariaActivity::class.java)
            startActivity(intent)
        }

        spinnerBuscarRegion()

        txtBuscarRegion.doAfterTextChanged {
            if (txtBuscarRegion.text.isEmpty())
                callServiceGetRegiones()
        }
    }

    fun actualizarRecyclerView(listaRegiones: List<RegionDataCollectionItem>? = null) {
        adapter = RecyclerAdapterRegion(listaRegiones, this@VerRegionSanitariaActivity)
        binding.contentRegion.recyclerViewRegion.adapter = adapter
    }

    fun callServiceGetRegiones() {
        val regionService: RegionService = RestEngine.buildService().create(RegionService::class.java)
        val result: Call<List<RegionDataCollectionItem>> = regionService.listRegiones()
        result.enqueue(object : Callback<List<RegionDataCollectionItem>> {
            override fun onFailure(call: Call<List<RegionDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerRegionSanitariaActivity,
                    "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<RegionDataCollectionItem>>,
                response: Response<List<RegionDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        }
        )
    }

    private fun callServiceGetRegion() {
        val regionService: RegionService = RestEngine.buildService()
            .create(RegionService::class.java)
        val result: Call<RegionDataCollectionItem> = regionService
            .getRegionById(txtBuscarRegion.text.toString().toLong())
        result.enqueue(object : Callback<RegionDataCollectionItem> {
            override fun onFailure(call: Call<RegionDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerRegionSanitariaActivity, "Error",
                    Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<RegionDataCollectionItem>,
                response: Response<RegionDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerRegionSanitariaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerRegionSanitariaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerRegionSanitariaActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    fun callServiceDeleteRegion(listaRegiones:List<RegionDataCollectionItem>?,
                                position: Int) {
        val regionService: RegionService = RestEngine.buildService()
            .create(RegionService::class.java)
        val result: Call<ResponseBody> = regionService
            .deleteRegionSanitaria(listaRegiones!![position].id_region!!)
        result.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerRegionSanitariaActivity, "Error",
                        Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@VerRegionSanitariaActivity,
                            "Eliminado Exitosamente", Toast.LENGTH_LONG).show()
                        callServiceGetRegiones()
                    } else if (response.code()==401) {
                        Toast.makeText(this@VerRegionSanitariaActivity,
                            "Sesion Expirada", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this@VerRegionSanitariaActivity,
                            "Fallo al traer el item", Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }

    fun enviar(context: Context, listaRegiones: List<RegionDataCollectionItem>?,
               position: Int) {
        val intent = Intent(context, RegistrarRegionSanitariaActivity::class.java)
        intent.putExtra("region", listaRegiones!![position])
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this,MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun spinnerBuscarRegion(){
        val spinner = findViewById<Spinner>(R.id.spnBuscarRegion)
        val lista = resources.getStringArray(R.array.spnBusquedaIDDepartamento)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner,lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(lista[p2] == "ID"){
                    binding.txtBuscarRegion.hint = "ID"
                    binding.txtBuscarRegion.setText("")
                    binding.txtBuscarRegion.inputType = 2
                    binding.btnBuscarRegion.setOnClickListener {
                        if(txtBuscarRegion.text.isNullOrEmpty()){
                            Toast.makeText(this@VerRegionSanitariaActivity,
                                "ID esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetRegion()
                        }
                    }

                }else if(lista[p2] == "Depto."){
                    binding.txtBuscarRegion.hint = "Departamento"
                    binding.txtBuscarRegion.setText("")
                    binding.txtBuscarRegion.inputType = 1
                    binding.btnBuscarRegion.setOnClickListener {
                        if(txtBuscarRegion.text.isNullOrEmpty()){
                            Toast.makeText(this@VerRegionSanitariaActivity,
                                "Departamento esta Vacío!", Toast.LENGTH_LONG).show()
                        }else {
                            callServiceGetDepartamento()
                        }
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
    }
    private fun callServiceGetDepartamento() {
        val regionService: RegionService = RestEngine.buildService().create(RegionService::class.java)
        val result: Call<RegionDataCollectionItem> = regionService
            .getRegionByDepartamento(txtBuscarRegion.text.toString())
        result.enqueue(object: Callback<RegionDataCollectionItem>{
            override fun onFailure(call: Call<RegionDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerRegionSanitariaActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<RegionDataCollectionItem>,
                response: Response<RegionDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerRegionSanitariaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerRegionSanitariaActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerRegionSanitariaActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
}

