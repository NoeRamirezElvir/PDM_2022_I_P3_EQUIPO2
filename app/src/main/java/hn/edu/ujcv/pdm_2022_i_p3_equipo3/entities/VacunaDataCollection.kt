package hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate

class VacunaDataCollection : ArrayList<VacunaDataCollectionItem>()
@Parcelize
data class VacunaDataCollectionItem(
    val id_vacuna : Long?,
    val id_fabricante: Long,
    val nombre: String,
    val numeroLote: Long,
    val fechaFabricacion : String,
    val fechaVencimiento : String,
    val fechaLlegada : String
):Parcelable
