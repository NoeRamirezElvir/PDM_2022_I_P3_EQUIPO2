package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CivilDataCollection : ArrayList<CivilDataCollectionItem>()
@Parcelize
data class CivilDataCollectionItem (
    val id_civil: Long?,
    val dni:Long,
    val nombre:String,
    val fechaNacimiento: String,
    val direccion:String,
    val telefono:String,
    val sexo: String
):Parcelable
