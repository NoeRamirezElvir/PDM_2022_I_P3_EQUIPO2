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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerComorbilidadesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_comorbilidad.view.*

import kotlinx.android.synthetic.main.card_layout_region.view.*

class RecyclerAdapterComorbilidad(pListaComorbs:List<ComorbilidadDataCollectionItem>? = null, activity: VerComorbilidadesActivity): RecyclerView.Adapter<RecyclerAdapterComorbilidad.ViewHolder>() {
    var listaComorbs: List<ComorbilidadDataCollectionItem>? = pListaComorbs
    var message: String = ""
    var activity2 : VerComorbilidadesActivity = activity

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenComorb)
        var itemIdComorb: TextView = itemView.findViewById(R.id.itemIdComorbilidad)
        var itemNombreo: TextView = itemView.findViewById(R.id.itemNombreComorbilidad)
        var itemTipo: TextView = itemView.findViewById(R.id.itemTipoComorbilidad)

        init {
            itemView.itemBtnBorrarComorb.setOnClickListener {
                val position : Int = adapterPosition
                message = listaComorbs!![position].nombre + "\nTipo:" + listaComorbs!![position].tipo +
                        "\n ID:" + listaComorbs!![position].id_comorbilidad
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar esta Comorbilidad?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteComorb(listaComorbs!!,position)
                        activity2.callServiceGetComorbs()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaComorbs!![position].id_comorbilidad, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.itemBtnEditarComorb.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaComorbs,position)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterComorbilidad.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_comorbilidad, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterComorbilidad.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.img_cd_comorbilidad)
        holder.itemIdComorb.text = "Comorbilidad No. " + listaComorbs!![position].id_comorbilidad
        holder.itemNombreo.text = listaComorbs!![position].nombre
        holder.itemTipo.text = "Tipo: "+listaComorbs!![position].tipo
    }

    override fun getItemCount(): Int {
        return listaComorbs!!.size
    }
}
