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
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCargo
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCargosBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CargosService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_empleado.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCargosActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerCargosBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCargo.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCargosBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarVerCargos)
        //
        callServiceGetCargos()
        binding.fabActualizarCargos.setOnClickListener {
            callServiceGetCargos()
        }
        layoutManager = LinearLayoutManager(this)
        binding.contentCargos.recyclerViewCargos.layoutManager = layoutManager
        //
        binding.fabCargos.setOnClickListener {
            val intent = Intent(this, RegistrarCargoActivity::class.java)
            startActivity(intent)
        }
        spinnerBusqueda()
        binding.txtBuscarCargo.doAfterTextChanged {
            if (binding.txtBuscarCargo.text.isNullOrEmpty()) {
                callServiceGetCargos()
            }
        }
    }

    fun actualizarRecyclerView(lista: List<CargosDataCollectionItem>) {
        adapter = RecyclerAdapterCargo(lista, this@VerCargosActivity)
        binding.contentCargos.recyclerViewCargos.adapter = adapter
    }

    fun callServiceGetCargos() {
        val cargosService: CargosService =
            RestEngine.buildService().create(CargosService::class.java)
        val result: Call<List<CargosDataCollectionItem>> = cargosService.listCargos()
        result.enqueue(object : Callback<List<CargosDataCollectionItem>> {
            override fun onFailure(call: Call<List<CargosDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerCargosActivity, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<List<CargosDataCollectionItem>>,
                response: Response<List<CargosDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!!)
            }
        })
    }

    private fun callServiceGetCargoNombre() {
        val cargosService: CargosService =
            RestEngine.buildService().create(CargosService::class.java)
        val result: Call<CargosDataCollectionItem> =
            cargosService.getCargoByNombre(binding.txtBuscarCargo.text.toString())
        result.enqueue(object : Callback<CargosDataCollectionItem> {
            override fun onFailure(call: Call<CargosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCargosActivity, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CargosDataCollectionItem>,
                response: Response<CargosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCargosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCargosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCargosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun callServiceGetCargoID() {
        val cargosService: CargosService =
            RestEngine.buildService().create(CargosService::class.java)
        val result: Call<CargosDataCollectionItem> =
            cargosService.getCargoById(binding.txtBuscarCargo.text.toString().toInt())
        result.enqueue(object : Callback<CargosDataCollectionItem> {
            override fun onFailure(call: Call<CargosDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCargosActivity, "Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(
                call: Call<CargosDataCollectionItem>,
                response: Response<CargosDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(listOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCargosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),
                            RestApiError::class.java)
                        Toast.makeText(this@VerCargosActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCargosActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    //Eliminar
    fun callServiceDeleteCargo(listaCarnet: List<CargosDataCollectionItem>?, position: Int) {
        val cargosService: CargosService =
            RestEngine.buildService().create(CargosService::class.java)
        val result: Call<ResponseBody> =
            cargosService.deleteCargo(listaCarnet!![position].id_cargo!!)
        result.enqueue(
            object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCargosActivity, "Error:", Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(
                                this@VerCargosActivity,
                                "Eliminado exitosamente",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        response.code() == 401 -> {
                            Toast.makeText(
                                this@VerCargosActivity,
                                "Sesion Expirada",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            Toast.makeText(
                                this@VerCargosActivity,
                                "Fallo al traer el item o se esta usando en otro registro",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        )
    }

    //Enviar datos
    fun enviar(context: Context, listaCargos: List<CargosDataCollectionItem>?, position: Int) {
        val intent = Intent(context, RegistrarCargoActivity::class.java)
        intent.putExtra("cargo", listaCargos!![position])
        startActivity(intent)
        this.finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MenuPrincipalActivity::class.java)
        startActivity(intent)
        this.finish()
    }
    //
    private fun spinnerBusqueda() {
        val spinner = findViewById<Spinner>(R.id.spnIDNombreBuscarCargo)
        val lista = resources.getStringArray(R.array.spnBusquedaIDNombre)
        val adaptador = ArrayAdapter(this, R.layout.color_spinner, lista)
        spinner.adapter = adaptador
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (lista[p2] == "ID") {
                    binding.txtBuscarCargo.hint = "ID"
                    binding.txtBuscarCargo.setText("")
                    binding.txtBuscarCargo.inputType = 2
                    binding.btnBuscarCargo.setOnClickListener {
                        if (binding.txtBuscarCargo.text.isNullOrEmpty()) {
                            Toast.makeText(
                                this@VerCargosActivity,
                                "ID esta Vacío",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            callServiceGetCargoID()
                        }
                    }

                } else if (lista[p2] == "Nombre") {
                    binding.txtBuscarCargo.hint = "Nombre"
                    binding.txtBuscarCargo.setText("")
                    binding.txtBuscarCargo.inputType = 1
                    binding.btnBuscarCargo.setOnClickListener {
                        if (binding.txtBuscarCargo.text.isNullOrEmpty()) {
                            Toast.makeText(
                                this@VerCargosActivity,
                                "Nombre esta Vacío",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            callServiceGetCargoNombre()
                        }
                    }
                }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }
}