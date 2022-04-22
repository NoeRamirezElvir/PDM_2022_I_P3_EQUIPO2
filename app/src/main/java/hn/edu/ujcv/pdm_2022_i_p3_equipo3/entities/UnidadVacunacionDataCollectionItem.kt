package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class UnidadVacunacionDataCollection: ArrayList<UnidadVacunacionDataCollectionItem>()
@Parcelize
data class UnidadVacunacionDataCollectionItem(val id_unidad:Long?,
                                              val id_centro: Long,
                                              val id_vacuna_suministrar: Long,
                                              val tipo:String): Parcelable
