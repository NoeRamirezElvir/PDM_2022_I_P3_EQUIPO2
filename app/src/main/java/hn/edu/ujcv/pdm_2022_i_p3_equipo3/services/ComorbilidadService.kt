package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ComorbilidadService {
    @GET("comorbilidad")
    fun listComorbs(): Call<List<ComorbilidadDataCollectionItem>>
    @GET("comorbilidad/id/{id}")
    fun getComorbById(@Path("id") id : Long) : Call<ComorbilidadDataCollectionItem>
    @GET("comorbilidad/nombre/{nombre}")
    fun getComorbByNombre(@Path("nombre") nombre : String) : Call<ComorbilidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @POST("comorbilidad/addComorbilidad")
    fun addComorb(@Body comorbData : ComorbilidadDataCollectionItem): Call<ComorbilidadDataCollectionItem>
    @Headers("Content-Type: application/json")
    @PUT("comorbilidad")
    fun updateComorb(@Body comorbilidadData: ComorbilidadDataCollectionItem): Call<ComorbilidadDataCollectionItem>
    @DELETE("comorbilidad/delete/{id}")
    fun deleteComorb(@Path("id") id: Long) : Call<ResponseBody>
}