package hn.edu.ujcv.pdm_2022_i_p3_equipo3


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters.RecyclerAdapterCivil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RestApiError
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.databinding.ActivityVerCivilesBinding
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.CivilService
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.services.RestEngine
import kotlinx.android.synthetic.main.activity_ver_civiles.*
import kotlinx.android.synthetic.main.card_layout_civil.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerCivilesActivity : AppCompatActivity() {
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<RecyclerAdapterCivil.ViewHolder>? = null
    private var pLista: List<CivilDataCollectionItem>? = null
    private lateinit var binding: ActivityVerCivilesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerCivilesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbarCiviles)
        //
        layoutManager = LinearLayoutManager(this)
        binding.contentCiviles.recyclerCiviles.layoutManager = layoutManager
        callServiceGetCiviles()
        //-----------------------------------
        binding.fab.setOnClickListener {
            val intent= Intent(this,RegistrarCivilActivity::class.java)
            startActivity(intent)
        }
        binding.fabListarCiviles.setOnClickListener { v ->
            callServiceGetCiviles()
        }
        imbtnBuscarCivil.setOnClickListener {v ->
            if(txtBuscarIdCiviles.text.isNullOrEmpty()){
                Toast.makeText(this@VerCivilesActivity,"ID esta Vacío", Toast.LENGTH_LONG).show()
            }else
                callServiceGetCivil()
        }
    }
    fun actualizarRecyclerView(pLista: List<CivilDataCollectionItem>?){
        adapter = RecyclerAdapterCivil(pLista, this)
        binding.contentCiviles.recyclerCiviles.adapter = adapter
    }
    fun callServiceGetCiviles() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<List<CivilDataCollectionItem>> = civilService.listCiviles()
        result.enqueue(
            object : Callback<List<CivilDataCollectionItem>> {
                override fun onFailure(call: Call<List<CivilDataCollectionItem>>, t: Throwable) {
                    Toast.makeText(this@VerCivilesActivity,"Error", Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<List<CivilDataCollectionItem>>,
                    response: Response<List<CivilDataCollectionItem>>
                ) {
                    actualizarRecyclerView(response.body())
                }
            }
        )
    }
    private fun callServiceGetCivil() {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<CivilDataCollectionItem> = civilService.getCivilById(txtBuscarIdCiviles.text.toString().toLong())
        result.enqueue(object : Callback<CivilDataCollectionItem>{
                override fun onFailure(call: Call<CivilDataCollectionItem>, t: Throwable) {

                    Toast.makeText(this@VerCivilesActivity,"Error",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(
                    call: Call<CivilDataCollectionItem>,
                    response: Response<CivilDataCollectionItem>
                ) {
                    if (response.isSuccessful){
                        actualizarRecyclerView(listOf(response.body()!!))
                    }else if (response.code() == 500){
                        val errorResponse = Gson().fromJson(response.errorBody()!!.string(), RestApiError::class.java)
                        Toast.makeText(this@VerCivilesActivity,errorResponse.errorDetails, Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerCivilesActivity,"Error", Toast.LENGTH_LONG).show()
                    }

                    //Toast.makeText(this@VerCivilesActivity,"OK Id: "+ response.body()!!.nombre,Toast.LENGTH_LONG).show()
                }
            }
        )
    }
    fun callServiceDeleteCivil(listaCivil : List<CivilDataCollectionItem>?, position: Int) {
        val civilService: CivilService = RestEngine.buildService().create(CivilService::class.java)
        val result: Call<ResponseBody> = civilService.deleteCivil(listaCivil!![position].id_civil!!)
        result.enqueue(
            object : Callback<ResponseBody>{
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Toast.makeText(this@VerCivilesActivity,"Error:",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.isSuccessful){
                        Toast.makeText(this@VerCivilesActivity,"Eliminado exitosamente",Toast.LENGTH_LONG).show()
                    }
                    else if(response.code()==401){
                        Toast.makeText(this@VerCivilesActivity,"Sesion Expirada",Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(this@VerCivilesActivity,"Fallo al traer el item",Toast.LENGTH_LONG).show()
                    }
                }
            }
        )
    }
    fun enviar(context: Context,listaCivil : List<CivilDataCollectionItem>? ,position :Int){
        val intent = Intent(context,RegistrarCivilActivity::class.java)
        intent.putExtra("civil",listaCivil!![position])
        startActivity(intent)
    }

}
