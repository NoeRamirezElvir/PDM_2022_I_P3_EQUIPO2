package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.FabricanteDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface FabricanteService {
    @GET("fabricantes")
    fun listFabs(): Call<List<FabricanteDataCollectionItem>>
    @GET("fabricantes/id/{id}")
    fun getFabById(@Path("id") id : Long) : Call<FabricanteDataCollectionItem>
    @GET("fabricantes/laboratorio/{lab}")
    fun getFabByNombre(@Path("lab") nombre : String) : Call<FabricanteDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("fabricantes/addFab")
    fun addFab(@Body fabData : FabricanteDataCollectionItem): Call<FabricanteDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("fabricantes")
    fun updateFab(@Body comorbilidadData: FabricanteDataCollectionItem): Call<FabricanteDataCollectionItem>
    @DELETE("fabricantes/delete/{id}")
    fun deleteFab(@Path("id") id: Long) : Call<ResponseBody>
}