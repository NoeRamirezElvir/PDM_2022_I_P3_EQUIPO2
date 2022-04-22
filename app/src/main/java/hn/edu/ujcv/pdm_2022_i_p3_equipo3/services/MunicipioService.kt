package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MunicipioService {

    @GET("municipios")
    fun listMunicipios(): Call<List<MunicipioDataCollectionItem>>

    @GET("municipios/id/{id}")
    fun getMunicipiosById(@Path("id") id:Long): Call<MunicipioDataCollectionItem>

    @GET("municipios/nombre/{nombre}")
    fun getMunicipioByNombre(@Path("nombre") nombre:String):Call<MunicipioDataCollectionItem>

    @Headers("Content-Type: application/json")
    @POST("municipios/addMunicipio")
    fun addMunicipio(@Body municipioData: MunicipioDataCollectionItem): Call<MunicipioDataCollectionItem>

    @Headers("Content-Type: application/json")
    @PUT("municipios")
    fun updateMunicipio(@Body municipioData: MunicipioDataCollectionItem): Call<MunicipioDataCollectionItem>

    @DELETE("municipios/delete/{id}")
    fun deleteMunicipio(@Path("id") id:Long): Call<ResponseBody>
}
