package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CargosService {
    @GET("cargos")
    fun listCargos(): Call<List<CargosDataCollectionItem>>
    @GET("cargos/id/{id}")
    fun getCargoById(@Path("id") id:Int): Call<CargosDataCollectionItem>
    @GET("cargos/nombre/{nombre}")
    fun getCargoByNombre(@Path("nombre") nombre:String): Call<CargosDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("cargos/addCargo")
    fun addCargo(@Body cargosData: CargosDataCollectionItem): Call<CargosDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("cargos")
    fun updateCargo(@Body cargosData: CargosDataCollectionItem): Call<CargosDataCollectionItem>
    @DELETE("cargos/delete/{id}")
    fun deleteCargo(@Path("id") id:Int): Call<ResponseBody>
}