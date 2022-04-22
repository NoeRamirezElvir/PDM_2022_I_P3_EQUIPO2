package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class PaisesDataCollection :ArrayList<PaisesDataCollectionItem>()
@Parcelize
data class PaisesDataCollectionItem(
    val id_pais: Long?,
    val codigo_iso:String,
    val nombre:String,
    val codigo_area:String
): Parcelable
