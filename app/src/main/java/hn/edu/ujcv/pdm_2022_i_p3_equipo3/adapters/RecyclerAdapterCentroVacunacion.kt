package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CentroVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RegionSanitaria
import kotlinx.android.synthetic.main.card_layout_carnet.view.*
import kotlinx.android.synthetic.main.card_layout_centro_vacunacion.view.*

class RecyclerAdapterCentroVacunacion(pListaCentros:ArrayList<CentroVacunacion>? = null): RecyclerView.Adapter<RecyclerAdapterCentroVacunacion.ViewHolder>() {
    var listaCentros:ArrayList<CentroVacunacion>? = pListaCentros

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:ImageView = itemView.findViewById(R.id.itemImagenCentro)
        var itemIdCentro:TextView = itemView.findViewById(R.id.itemIdCentro)
        var itemNbeCentro:TextView = itemView.findViewById(R.id.itemNombreCentro)
        var itemDireccion:TextView = itemView.findViewById(R.id.itemDireccionCentro)
        var itemHoras: TextView = itemView.findViewById(R.id.itemHorario)
        var itemTipo:TextView = itemView.findViewById(R.id.itemTipo)
        init {
            itemView.itemBtnBorrarCentro.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarCentro.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCentroVacunacion.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_centro_vacunacion, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCentroVacunacion.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.ic_vacunacion)
        holder.itemIdCentro.text = "Centro de Vacunación No.7"
        holder.itemNbeCentro.text = "Cuida tu Salud"
        holder.itemDireccion.text = "Dirección: Frente a Iglesia Católica"
        holder.itemHoras.text = "Lunes-Viernes  08:00am-03:00pm"
        holder.itemTipo.text = "Centro de Vacunación Público"
    }

    override fun getItemCount(): Int {
        return 1
    }


}