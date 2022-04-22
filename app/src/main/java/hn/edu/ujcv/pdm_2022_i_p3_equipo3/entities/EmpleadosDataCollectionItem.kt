package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

class EmpleadosDataCollection: ArrayList<EmpleadosDataCollectionItem>()
@Parcelize
data class EmpleadosDataCollectionItem(val id_empleado:Int? = 0,
                                       val codigo:Long,
                                       val dni: String,
                                       var nombre:String,
                                       val telefono:Long,
                                       val correo: String,
                                       var password: String,
                                       val id_cargo:Int,
                                       val id_unidad:Int):Parcelable
