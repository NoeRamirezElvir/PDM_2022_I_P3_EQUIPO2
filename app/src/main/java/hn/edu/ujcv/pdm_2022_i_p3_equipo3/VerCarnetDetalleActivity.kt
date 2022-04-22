package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetDetalleBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetDetallesDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CarnetDetalleService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CarnetEncabezadoService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
        callServiceGetCarnetDetalle()
        binding.imbtBuscarDetalle.setOnClickListener {
            if(binding.txtBuscarIdCarnetDetalle.text.isNullOrEmpty()){
                Toast.makeText(this@VerCarnetDetalleActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
            }else{
                callServiceGetCarnetDetalleID()
            }
        }
        binding.txtBuscarIdCarnetDetalle.doAfterTextChanged {
            if(binding.txtBuscarIdCarnetDetalle.text.isNullOrEmpty()){
                callServiceGetCarnetDetalle()
            }
        }
        //

    }
    fun actualizarRecyclerView(lista:ArrayList<CarnetDetallesDataCollectionItem>?){
        adapter = RecyclerAdapterCarnetDetalle(lista = lista, activity = this)
       binding.contentCarnetDetalle.recyclerCarnetDetalleD.adapter = adapter
    }
    fun callServiceGetCarnetDetalle() {
        val detalleService: CarnetDetalleService = RestEngine.buildService().create(
            CarnetDetalleService::class.java)
        val result: Call<List<CarnetDetallesDataCollectionItem>> = detalleService.listCarnetDetalle()
        result.enqueue(object: Callback<List<CarnetDetallesDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetDetallesDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetDetallesDataCollectionItem>>,
                response: Response<List<CarnetDetallesDataCollectionItem>>
            ) {
                actualizarRecyclerView(response.body()!! as ArrayList<CarnetDetallesDataCollectionItem>)
            }
        })
    }
    private fun callServiceGetCarnetDetalleID() {
        val carnetService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<CarnetDetallesDataCollectionItem> = carnetService.getCarnetDetalleById(binding.txtBuscarIdCarnetDetalle.text.toString().toInt())
        result.enqueue(object: Callback<CarnetDetallesDataCollectionItem>{
            override fun onFailure(call: Call<CarnetDetallesDataCollectionItem>, t: Throwable) {
                Toast.makeText(this@VerCarnetDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<CarnetDetallesDataCollectionItem>,
                response: Response<CarnetDetallesDataCollectionItem>
            ) {
                when {
                    response.isSuccessful -> {
                        actualizarRecyclerView(arrayListOf(response.body()!!))
                    }
                    response.code() == 500 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),RestApiError::class.java)
                        Toast.makeText(this@VerCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    response.code() == 404 -> {
                        val errorResponse= Gson().fromJson(response.errorBody()!!.string(),RestApiError::class.java)
                        Toast.makeText(this@VerCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this@VerCarnetDetalleActivity,"Error: ${response.errorBody()!!.string()}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }
    fun callServiceDeleteCarnetDetalle(listaCarnet : List<CarnetDetallesDataCollectionItem>?, position: Int) {
        val carnetService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<ResponseBody> = carnetService.deleteCarnetDetalle(listaCarnet!![position].id_carnetDetalle!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCarnetDetalleActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(this@VerCarnetDetalleActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@VerCarnetDetalleActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        response.code() == 500 -> {
                            val errorResponse= Gson().fromJson(response.errorBody()!!.string(),RestApiError::class.java)
                            Toast.makeText(this@VerCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        response.code() == 404 -> {
                            val errorResponse= Gson().fromJson(response.errorBody()!!.string(),RestApiError::class.java)
                            Toast.makeText(this@VerCarnetDetalleActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@VerCarnetDetalleActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }
}

