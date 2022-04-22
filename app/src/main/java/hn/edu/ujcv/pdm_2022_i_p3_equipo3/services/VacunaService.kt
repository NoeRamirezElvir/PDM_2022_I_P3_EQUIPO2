package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.VacunaDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface VacunaService {
    @GET("vacunas")
    fun listVacunas(): Call<List<VacunaDataCollectionItem>>
    @GET("vacunas/id/{id}")
    fun getVacunaById(@Path("id") id : Long) : Call<VacunaDataCollectionItem>
    @GET("vacunas/nombre/{nombre}")
    fun getVacunaByNombre(@Path("nombre") nombre : String) : Call<VacunaDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("vacunas/addVacuna")
    fun addVacuna(@Body vacunaData : VacunaDataCollectionItem): Call<VacunaDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("vacunas")
    fun updateVacuna(@Body vacunaData: VacunaDataCollectionItem): Call<VacunaDataCollectionItem>
    @DELETE("vacunas/delete/{id}")
    fun deleteVacuna(@Path("id") id: Long) : Call<ResponseBody>
}