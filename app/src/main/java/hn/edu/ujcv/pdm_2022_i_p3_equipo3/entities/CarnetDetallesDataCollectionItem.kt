package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class CarnetDetallesDataCollection: ArrayList<CarnetDetallesDataCollectionItem>()
@Parcelize
data class CarnetDetallesDataCollectionItem(val id_carnetDetalle:Int?,
                                            val id_carnetEncabezado:Int,
                                            val id_unidad:Int,
                                            val fecha: String,
                                            val dosis:String,
                                            val observacion:String,
                                            val idEmpleadoVacuno:Int):Parcelable
