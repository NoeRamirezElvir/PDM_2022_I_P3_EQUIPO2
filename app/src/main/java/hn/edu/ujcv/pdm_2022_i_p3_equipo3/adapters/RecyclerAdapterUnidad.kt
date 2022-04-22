package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerFabricantesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerUnidadesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.UnidadVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.UnidadDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_unidad_vacunacion.view.*

class RecyclerAdapterUnidad(pListaUnidad:List<UnidadDataCollectionItem>? = null, activity: VerUnidadesActivity) : RecyclerView.Adapter<RecyclerAdapterUnidad.ViewHolder>(){
    var listaUnidad: List<UnidadDataCollectionItem>? = pListaUnidad
    var message: String = ""
    var activity2 : VerUnidadesActivity = activity

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenUnidad)
        var itemIdUV: TextView = itemView.findViewById(R.id.itemIdUnidad)
        var itemCentroVU: TextView = itemView.findViewById(R.id.itemCentroVacunacionUnidad)
        var itemVacunaU: TextView = itemView.findViewById(R.id.itemVacunaIyectable)
        var itemTipoU: TextView = itemView.findViewById(R.id.itemTipoUnidad)
        init {
            itemView.itemBtnBorrarUnidad.setOnClickListener {
                val position : Int = adapterPosition
                message = "Unidad: "+listaUnidad!![position].id_unidad + "\nCentro de Vacunacion:"  + listaUnidad!![position].id_centro +
                        "\nTipo: " + listaUnidad!![position].tipo
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteUnidad(listaUnidad!!,position)
                        activity2.callServiceGetUnidades()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaUnidad!![position].id_unidad, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.itemBtnEditarUnidad.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaUnidad,position)
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
        holder.itemIdUV.text = "Unidad: " + listaUnidad!![position].id_unidad
        holder.itemCentroVU.text = "Centro de Vacunacion: " + listaUnidad!![position].id_centro
        holder.itemVacunaU.text = "Vacuna  que suministra: "+ listaUnidad!![position].id_vacuna_suministrar
        holder.itemTipoU.text = "Tipo de Unidad: "+ listaUnidad!![position].tipo
    }

    override fun getItemCount(): Int {
        return listaUnidad!!.size
    }
}