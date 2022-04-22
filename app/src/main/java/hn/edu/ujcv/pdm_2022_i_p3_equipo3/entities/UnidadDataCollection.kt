package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class UnidadDataCollection : ArrayList<UnidadDataCollectionItem>()
@Parcelize
data class UnidadDataCollectionItem(
    val id_unidad : Long?,
    val id_centro: Long,
    val id_vacuna_suministrar: Long,
    val tipo:String ):Parcelable
