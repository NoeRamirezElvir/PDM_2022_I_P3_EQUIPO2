package hn.edu.ujcv.pdm_2022_i_p3_equipo3.services

import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import retrofit2.Call
import retrofit2.http.GET

interface CentroVacunacionService {
    @GET("centrosVacunacion")
    fun listCentros(): Call<List<CentroVacunacionDataCollectionItem>>
}