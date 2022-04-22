package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class RegionDataCollection : ArrayList<RegionDataCollectionItem>()

@Parcelize
data class RegionDataCollectionItem (
    val id_region: Long?,
    val departamento:String,
    val jefatura:String,
    val telefono:Long
):Parcelable
