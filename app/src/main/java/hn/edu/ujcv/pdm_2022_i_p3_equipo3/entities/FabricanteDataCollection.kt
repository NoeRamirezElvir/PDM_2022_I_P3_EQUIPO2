package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class FabricanteDataCollection : ArrayList<FabricanteDataCollectionItem>()
@Parcelize
data class FabricanteDataCollectionItem(
    val id_fabricante : Long?,
    val laboratorio: String,
    val nombre_contacto: String,
    val telefono_contacto: Long,
    val id_pais: Long
):Parcelable


