package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface CivilComorbilidadService {
    @GET("civil_comorbilidad")
    fun listCivsComorbs(): Call<List<CivilComorbilidadDataCollectionItem>>
    @GET("civil_comorbilidad/id/{id}")
    fun getCivComorbById(@Path("id") id : Long) : Call<CivilComorbilidadDataCollectionItem>
    @GET("civil_comorbilidad/estado/{estado}")
    fun getCivComorbByEstado(@Path("estado") nombre : String) : Call<CivilComorbilidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("civil_comorbilidad/addCivilComorbilidad")
    fun addCivComorb(@Body civComorbData : CivilComorbilidadDataCollectionItem): Call<CivilComorbilidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("civil_comorbilidad")
    fun updateCivComorb(@Body cibComorbilidadData: CivilComorbilidadDataCollectionItem): Call<CivilComorbilidadDataCollectionItem>
    @DELETE("civil_comorbilidad/delete/{id}")
    fun deleteCivComorb(@Path("id") id: Long) : Call<ResponseBody>
}