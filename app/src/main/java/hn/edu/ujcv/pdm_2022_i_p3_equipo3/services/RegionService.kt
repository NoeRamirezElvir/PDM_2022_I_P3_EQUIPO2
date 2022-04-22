package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface RegionService {

    @GET("regiones")
    fun listRegiones(): Call<List<RegionDataCollectionItem>>

    @GET("regiones/id/{id}")
    fun getRegionById(@Path("id") id:Long): Call<RegionDataCollectionItem>

    @GET("regiones/departamento/{departamento}")
    fun getRegionByDepartamento(@Path("departamento") departamento:String): Call<RegionDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("regiones/addRegionSanitaria")
    fun addRegionSanitaria(@Body regionData:RegionDataCollectionItem):Call<RegionDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("regiones")
    fun updateRegionSanitaria(@Body regionData:RegionDataCollectionItem):Call<RegionDataCollectionItem>

    @DELETE("regiones/delete/{id}")
    fun deleteRegionSanitaria(@Path("id") id:Long):Call<ResponseBody>
}
