package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CivilesService {
    @GET("civiles")
    fun listCiviles(): Call<List<CivilDataCollectionItem>>
    @GET("civiles/id/{id}")
    fun getCivilById(@Path("id") id:Long): Call<CivilDataCollectionItem>
    @GET("civiles/nombre/{nombre}")
    fun getCivilByNombre(@Path("nombre") nombre:String): Call<CivilDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("civiles/addEmpleado")
    fun addCivil(@Body civilData: CivilDataCollectionItem): Call<CivilDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("civiles")
    fun updateCivil(@Body civilData: CivilDataCollectionItem): Call<CivilDataCollectionItem>
    @DELETE("civiles/delete/{id}")
    fun deleteCivil(@Path("id") id:Long): Call<ResponseBody>
}