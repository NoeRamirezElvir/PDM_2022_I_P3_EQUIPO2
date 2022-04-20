package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class ComorbilidadDataCollection : ArrayList<ComorbilidadDataCollectionItem>()
@Parcelize
data class ComorbilidadDataCollectionItem(
    val id: Long?,
    val nombre:String,
    val tipo: String
) : Parcelable
