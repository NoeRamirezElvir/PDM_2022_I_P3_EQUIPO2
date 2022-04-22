package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.UnidadDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UnidadService {
    @GET("unidadVacunacion")
    fun listUnidades(): Call<List<UnidadDataCollectionItem>>
    @GET("unidadVacunacion/id/{id}")
    fun getUnidadById(@Path("id") id : Long) : Call<UnidadDataCollectionItem>
    @GET("unidadVacunacion/tipo/{tipo}")
    fun getUnidadByNombre(@Path("tipo") tipo : String) : Call<UnidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("unidadVacunacion/addUnidad")
    fun addUnidad(@Body unidadData : UnidadDataCollectionItem): Call<UnidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("unidadVacunacion")
    fun updateUnidad(@Body unidadData: UnidadDataCollectionItem): Call<UnidadDataCollectionItem>
    @DELETE("unidadVacunacion/delete/{id}")
    fun deleteUnidad(@Path("id") id: Long) : Call<ResponseBody>
}