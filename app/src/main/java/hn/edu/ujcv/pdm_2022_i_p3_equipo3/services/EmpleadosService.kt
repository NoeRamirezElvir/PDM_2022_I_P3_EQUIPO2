package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface EmpleadosService {
    @GET("empleados")
    fun listEmpleados(): Call<List<EmpleadosDataCollectionItem>>
    @GET("empleados/id/{id}")
    fun getEmpleadoById(@Path("id") id:Int): Call<EmpleadosDataCollectionItem>
    @GET("empleados/nombre/{nombre}")
    fun getEmpleadoByNombre(@Path("nombre") nombre:String): Call<EmpleadosDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("empleados/addEmpleado")
    fun addEmpleado(@Body empleadoData: EmpleadosDataCollectionItem): Call<EmpleadosDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("empleados")
    fun updateEmpleado(@Body empleadoData: EmpleadosDataCollectionItem): Call<EmpleadosDataCollectionItem>
    @DELETE("empleados/delete/{id}")
    fun deleteEmpleado(@Path("id") id:Int): Call<ResponseBody>
}