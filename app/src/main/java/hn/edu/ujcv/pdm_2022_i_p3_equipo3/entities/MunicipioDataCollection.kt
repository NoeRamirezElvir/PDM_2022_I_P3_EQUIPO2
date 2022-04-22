package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class MunicipioDataCollection : ArrayList<MunicipioDataCollectionItem>()

@Parcelize
data class MunicipioDataCollectionItem(
    val id_municipio:Long?,
    val nombre:String,
    val id_region:Long):Parcelable
