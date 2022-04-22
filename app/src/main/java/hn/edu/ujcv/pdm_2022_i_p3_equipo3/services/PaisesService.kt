package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.PaisesDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface PaisesService {

    @GET("paises")
    fun listPaises(): Call<List<PaisesDataCollectionItem>>

    @GET("paises/id/{id}")
    fun getPaisesById(@Path("id") id:Long): Call<PaisesDataCollectionItem>

    @GET("paises/nombre/{nombre}")
    fun getPaisesByNombre(@Path("nombre") nombre:String): Call<PaisesDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("paises/addPais")
    fun addPais(@Body paisData: PaisesDataCollectionItem): Call<PaisesDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("paises")
    fun updatePais(@Body paisData: PaisesDataCollectionItem): Call<PaisesDataCollectionItem>

    @DELETE("paises/delete/{id}")
    fun deletePais(@Path("id") id:Long): Call<ResponseBody>

}
