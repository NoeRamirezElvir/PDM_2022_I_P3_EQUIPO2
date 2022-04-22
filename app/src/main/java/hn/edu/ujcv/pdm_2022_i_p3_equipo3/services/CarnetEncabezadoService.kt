package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetEncabezadoDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CarnetEncabezadoService {
    @GET("carnetEncabezado")
    fun listCarnetEncabezado(): Call<List<CarnetEncabezadoDataCollectionItem>>
    @GET("carnetEncabezado/id/{id}")
    fun getCarnetEncabezadoById(@Path("id") id:Long): Call<CarnetEncabezadoDataCollectionItem>
    @GET("carnetEncabezado/numeroCarnet/{numeroCarnet}")
    fun getCarnetEncabezadoByNumeroCarnet(@Path("numeroCarnet") numeroCarnet:Long): Call<CarnetEncabezadoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("carnetEncabezado/addCarnetEncabezado")
    fun addCarnetEncabezado(@Body empleadoData: CarnetEncabezadoDataCollectionItem): Call<CarnetEncabezadoDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("carnetEncabezado")
    fun updateCarnetEncabezado(@Body empleadoData: CarnetEncabezadoDataCollectionItem): Call<CarnetEncabezadoDataCollectionItem>
    @DELETE("carnetEncabezado/delete/{id}")
    fun deleteCarnetEncabezado(@Path("id") id:Long): Call<ResponseBody>
}