package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CivilComorbilidadDataCollection : ArrayList<CivilComorbilidadDataCollectionItem>()

@Parcelize
data class CivilComorbilidadDataCollectionItem(
    val id_civilComorbilidad: Long?,
    val id_civil:Long,
    val id_comorbilidad: Long,
    val estado : String,
    val observacion: String
):Parcelable
