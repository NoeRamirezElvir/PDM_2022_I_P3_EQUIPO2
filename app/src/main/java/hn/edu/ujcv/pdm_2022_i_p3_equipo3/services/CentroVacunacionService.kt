package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CentroVacunacionService {

    @GET("centrosVacunacion")
    fun listCentrosVacunacion(): Call<List<CentroVacunacionDataCollectionItem>>

    @GET("centrosVacunacion/id/{id}")
    fun getCentrosVacunacionById(@Path("id") id:Long): Call<CentroVacunacionDataCollectionItem>

    @GET("centrosVacunacion/nombre/{nombre}")
    fun getCentrosVacunacionByNombre(@Path("nombre") nombre:String): Call<CentroVacunacionDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("centrosVacunacion/addCentro")
    fun addCentro(@Body centroVacunacionData: CentroVacunacionDataCollectionItem):
            Call<CentroVacunacionDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("centrosVacunacion")
    fun updateCentro(@Body centroVacunacionData: CentroVacunacionDataCollectionItem):
            Call<CentroVacunacionDataCollectionItem>

    @DELETE("centrosVacunacion/delete/{id}")
    fun deleteCentro(@Path("id") id:Long): Call<ResponseBody>

}
