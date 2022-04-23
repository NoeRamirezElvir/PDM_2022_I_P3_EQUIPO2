package hn.edu.ujcv.pdm_2022_i_p3_equipo3

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCarnetMasDetalleBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetDetallesDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CarnetDetalleService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CarnetEncabezadoService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCarnetMasDetalleActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>? = null
    private lateinit var binding:ActivityVerCarnetMasDetalleBinding
    private var listaDetalles = arrayListOf<CarnetDetallesDataCollectionItem>()
    private lateinit var encabezado:CarnetEncabezadoDataCollectionItem
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCarnetMasDetalleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        layoutManager = LinearLayoutManager(this)
        binding.contentCarnetMasDetalle.recyclerCarnetDetalleD.layoutManager = layoutManager

        obtenerCarnetEncabezado()
        callServiceGetCarnetDetalle()
        binding.imvButtonActualizarEncabezadoMasDetalles.setOnClickListener { callServiceGetCarnetDetalle() }

    }
    fun actualizarRecyclerView(lista:ArrayList<CarnetDetallesDataCollectionItem>){
        adapter = RecyclerAdapterCarnetDetalle(lista, activity2 = this)
        binding.contentCarnetMasDetalle.recyclerCarnetDetalleD.adapter = adapter
    }
    //obtenerDatos
    @SuppressLint("SetTextI18n")
    private fun obtenerCarnetEncabezado() {
        val intent = intent
        val objeto: CarnetEncabezadoDataCollectionItem? = intent.getParcelableExtra("encabezado")
        if (objeto != null) {
            encabezado = objeto
            binding.textView8.text = "NO. ${objeto.numeroCarnet}"
            binding.textView6.text = "ID del carnet: ${objeto.id_carnetEncabezado}"
            binding.textView7.text = "ID del Paciente: ${objeto.id_civil}"
        }
    }
    fun callServiceGetCarnetDetalle() {
        val detalleService: CarnetDetalleService = RestEngine.buildService().create(
            CarnetDetalleService::class.java)
        val result: Call<List<CarnetDetallesDataCollectionItem>> = detalleService.listCarnetDetalle()
        result.enqueue(object: Callback<List<CarnetDetallesDataCollectionItem>> {
            override fun onFailure(call: Call<List<CarnetDetallesDataCollectionItem>>, t: Throwable) {
                Toast.makeText(this@VerCarnetMasDetalleActivity,"Error", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(
                call: Call<List<CarnetDetallesDataCollectionItem>>,
                response: Response<List<CarnetDetallesDataCollectionItem>>
            ) {
                listaDetalles = arrayListOf()
                val detalles = response.body()!!
                for(item in detalles){
                    if(item.id_carnetEncabezado == encabezado.id_carnetEncabezado!!.toInt()){
                        listaDetalles.add(item)
                    }
                }
                actualizarRecyclerView(listaDetalles)
            }
        })
    }
    fun callServiceDeleteCarnetDetalle(listaCarnet : List<CarnetDetallesDataCollectionItem>?, position: Int) {
        val carnetService: CarnetDetalleService = RestEngine.buildService().create(CarnetDetalleService::class.java)
        val result: Call<ResponseBody> = carnetService.deleteCarnetDetalle(listaCarnet!![position].id_carnetDetalle!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCarnetMasDetalleActivity,"Error:",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    when {
                        response.isSuccessful -> {
                            Toast.makeText(this@VerCarnetMasDetalleActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                        }
                        response.code()==401 -> {
                            Toast.makeText(this@VerCarnetMasDetalleActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            Toast.makeText(this@VerCarnetMasDetalleActivity,"Fallo al traer el item o se esta usando en otro registro",Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        )
    }

}