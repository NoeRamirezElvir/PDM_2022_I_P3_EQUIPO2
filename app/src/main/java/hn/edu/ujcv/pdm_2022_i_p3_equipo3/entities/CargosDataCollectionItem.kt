package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class CargosDataCollection: ArrayList<CargosDataCollectionItem>()
@Parcelize
data class CargosDataCollectionItem(val id_cargo:Int?,
                                    val nombre:String,
                                    val descripcion:String):Parcelable
