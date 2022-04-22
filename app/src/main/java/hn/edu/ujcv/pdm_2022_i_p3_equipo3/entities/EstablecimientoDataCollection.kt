package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class EstablecimientoSaludDataCollection : ArrayList<EstablecimientoSaludDataCollection>()

@Parcelize
data class EstablecimientoSaludDataCollectionItem(
    val id_establecimiento:Long?,
    val nombre:String,
    val direccion:String,
    val telefono:Long,
    val id_municipio:Long
):Parcelable
