package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.UnidadVacunacion
import kotlinx.android.synthetic.main.card_layout_unidad_vacunacion.view.*

class RecyclerAdapterUnidad(pListaUnidad:ArrayList<UnidadVacunacion>? = null) : RecyclerView.Adapter<RecyclerAdapterUnidad.ViewHolder>(){
    var listaUnidad: ArrayList<UnidadVacunacion>? = pListaUnidad

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenUnidad)
        var itemIdUV: TextView = itemView.findViewById(R.id.itemIdUnidad)
        var itemCentroVU: TextView = itemView.findViewById(R.id.itemCentroVacunacionUnidad)
        var itemDocU: TextView = itemView.findViewById(R.id.itemDoctorA)
        var itemVacunaU: TextView = itemView.findViewById(R.id.itemVacunaIyectable)
        var itemvacunadorU: TextView = itemView.findViewById(R.id.itemVacunador)
        init {
            itemView.itemBtnBorrarUnidad.setOnClickListener {
                Toast.makeText(it.context, "Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarUnidad.setOnClickListener {
                Toast.makeText(it.context, "Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterUnidad.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_unidad_vacunacion, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterUnidad.ViewHolder, position: Int) {
        holder.itemImagen.setImageResource(R.drawable.img_cd_unidad_vacunacion)
        holder.itemIdUV.text = "Unidad: 1"
        holder.itemCentroVU.text = "Cis. San Miguel"
        holder.itemDocU.text = "Doctor Asignado: Julio Flores"
        holder.itemVacunaU.text = "Vacuna sumistrada: Sputnik"
        holder.itemvacunadorU.text = "Vacunador: Miguel Fonseca"
    }

    override fun getItemCount(): Int {
        return 1
    }
}