package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetDetallesDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CarnetDetalleService {
    @GET("carnetDetalle")
    fun listCarnetDetalle(): Call<List<CarnetDetallesDataCollectionItem>>
    @GET("carnetDetalle/id/{id}")
    fun getCarnetDetalleById(@Path("id") id:Int): Call<CarnetDetallesDataCollectionItem>
    @GET("carnetDetalle/observacion/{observacion}")
    fun getCarnetDetalleByObservacion(@Path("observacion") observacion:String): Call<CarnetDetallesDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("carnetDetalle/addCarnetDetalle")
    fun addCarnetDetalle(@Body detalleData: CarnetDetallesDataCollectionItem): Call<CarnetDetallesDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("carnetDetalle")
    fun updateCarnetDetalle(@Body detalleData: CarnetDetallesDataCollectionItem): Call<CarnetDetallesDataCollectionItem>
    @DELETE("carnetDetalle/delete/{id}")
    fun deleteCarnetDetalle(@Path("id") id:Int): Call<ResponseBody>
}