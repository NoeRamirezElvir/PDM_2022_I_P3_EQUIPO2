package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.UnidadVacunacionDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface UnidadService {
    @GET("unidadVacunacion")
    fun listUnidadVacunacion(): Call<List<UnidadVacunacionDataCollectionItem>>
    @GET("unidadVacunacion/id/{id}")
    fun getUnidadVacunacion(@Path("id") id:Long): Call<UnidadVacunacionDataCollectionItem>
    @GET("unidadVacunacion/tipo/{tipo}")
    fun getUnidadVacunacion(@Path("tipo") nombre:String): Call<UnidadVacunacionDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("unidadVacunacion/addUnidadVacunacion")
    fun addUnidadVacunacion(@Body unidadData: UnidadVacunacionDataCollectionItem): Call<UnidadVacunacionDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("unidadVacunacion")
    fun updateUnidadVacunacion(@Body unidadData: UnidadVacunacionDataCollectionItem): Call<UnidadVacunacionDataCollectionItem>
    @DELETE("unidadVacunacion/delete/{id}")
    fun deleteUnidadVacunacion(@Path("id") id:Long): Call<ResponseBody>
}