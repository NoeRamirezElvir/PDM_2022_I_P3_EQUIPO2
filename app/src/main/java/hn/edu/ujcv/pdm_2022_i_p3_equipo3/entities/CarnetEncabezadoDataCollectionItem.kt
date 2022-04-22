package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CarnetEncabezadoDataCollection:ArrayList<CarnetEncabezadoDataCollectionItem>()
@Parcelize
data class CarnetEncabezadoDataCollectionItem(val id_carnetEncabezado:Long?,
                                              val id_civil:Long,
                                              val numeroCarnet:Long):Parcelable
