package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EstablecimientoSaludDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EstablecimientoService {

    @GET("establecimientos")
    fun listEstablecimientos(): Call<List<EstablecimientoSaludDataCollectionItem>>

    @GET("establecimientos/id/{id}")
    fun getEstablecimientosById(@Path("id") id:Long): Call<EstablecimientoSaludDataCollectionItem>

    @GET("establecimientos/nombre/{nombre}")
    fun getEstablecimientosByNombre(@Path("nombre") nombre:String): Call<EstablecimientoSaludDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("establecimientos/addEstablecimiento")
    fun addEstablecimiento(@Body establecimientoData: EstablecimientoSaludDataCollectionItem):
            Call<EstablecimientoSaludDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("establecimientos")
    fun updateEstablecimiento(@Body establecimientoData: EstablecimientoSaludDataCollectionItem):
            Call<EstablecimientoSaludDataCollectionItem>

    @DELETE("establecimientos/delete/{id}")
    fun deleteEstablecimiento(@Path("id") id:Long): Call<ResponseBody>

}
