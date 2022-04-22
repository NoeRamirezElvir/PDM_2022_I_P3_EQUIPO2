package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CentroVacunacionDataCollection : ArrayList<CentroVacunacionDataCollectionItem>()
@Parcelize
data class CentroVacunacionDataCollectionItem(
    val id_centroVacunacion:Long?,
    val nombre:String,
    val direccion:String,
    val tipo:String,
    val horario:String,
    val id_establecimiento:Long
):Parcelable
