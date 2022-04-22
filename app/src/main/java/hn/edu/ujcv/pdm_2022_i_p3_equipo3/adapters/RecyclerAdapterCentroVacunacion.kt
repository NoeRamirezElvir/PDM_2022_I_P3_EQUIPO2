package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCentroVacunacionActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CentroVacunacionDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_centro_vacunacion.view.*

class RecyclerAdapterCentroVacunacion(pListaCentros:List<CentroVacunacionDataCollectionItem>? = null,
                                      activity_: VerCentroVacunacionActivity): RecyclerView.Adapter<RecyclerAdapterCentroVacunacion.ViewHolder>() {
    var listaCentros:List<CentroVacunacionDataCollectionItem>? = pListaCentros
    var message:String = ""
    var activity: VerCentroVacunacionActivity = activity_

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:ImageView = itemView.findViewById(R.id.itemImagenCentro)
        var itemIdCentro:TextView = itemView.findViewById(R.id.itemIdCentro)
        var itemNbeCentro:TextView = itemView.findViewById(R.id.itemNombreCentro)
        var itemDireccion:TextView = itemView.findViewById(R.id.itemDireccionCentro)
        var itemHoras: TextView = itemView.findViewById(R.id.itemHorario)
        var itemTipo:TextView = itemView.findViewById(R.id.itemTipo)
        init {
            itemView.itemBtnBorrarCentro.setOnClickListener {
                val position:Int = adapterPosition
                message = "Centro de Vacunacion: " + listaCentros!![position].nombre +
                        "\nDireccion: " + listaCentros!![position].direccion +
                        "\nHorario: " + listaCentros!![position].horario +
                        "\nTipo: " + listaCentros!![position].tipo +
                        "\nEstablecimiento No." + listaCentros!![position].id_establecimiento +
                        "\n ID: " + listaCentros!![position].id_centroVacunacion
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteCentro(listaCentros!!, position)
                    }
                    .setNegativeButton("No") {_,_ ->
                        Toast.makeText(it.context, "No se elimino el registro: " +
                                listaCentros!![position].id_centroVacunacion,
                            Toast.LENGTH_LONG).show()
                    }.create()
                dialog.show()
            }

            itemView.itemBtnEditarCentro.setOnClickListener {
                val position: Int = adapterPosition
                activity.enviar(it.context, listaCentros!!, position)
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
        holder.itemImagen.setImageResource(R.drawable.ic_vacunacion)
        holder.itemIdCentro.text = "Centro de Vacunación No." +
                listaCentros!![position].id_centroVacunacion
        holder.itemNbeCentro.text = listaCentros!![position].nombre
        holder.itemDireccion.text = "Dirección: " + listaCentros!![position].direccion
        holder.itemHoras.text = listaCentros!![position].horario
        holder.itemTipo.text = "Centro de Vacunación " + listaCentros!![position].tipo
    }

    override fun getItemCount(): Int {
        return listaCentros!!.size
    }


}
