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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerRegionSanitariaActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.RegionDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_region.view.*

class RecyclerAdapterRegion(pListaRegiones:List<RegionDataCollectionItem>? = null,
                            activity_: VerRegionSanitariaActivity):
    RecyclerView.Adapter<RecyclerAdapterRegion.ViewHolder>() {
    var listaRegiones:List<RegionDataCollectionItem>? = pListaRegiones
    var message:String = ""
    var activity: VerRegionSanitariaActivity = activity_

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:       ImageView = itemView.findViewById(R.id.itemImagenRegion)
        var itemIdRegion:     TextView = itemView.findViewById(R.id.itemIdRegion)
        var itemDepartamento: TextView = itemView.findViewById(R.id.itemDepartamento)
        var itemJefatura:     TextView = itemView.findViewById(R.id.itemJefaturaRegional)
        var itemTelefono:     TextView = itemView.findViewById(R.id.itemTelefonoRegion)

        init {
            itemView.itemBtnBorrarRegion.setOnClickListener {
                val position:Int = adapterPosition
                message = listaRegiones!![position].departamento +
                        "\nJefatura: " + listaRegiones!![position].jefatura +
                        "\nTelefono: " + listaRegiones!![position].telefono +
                        "\n ID: " + listaRegiones!![position].id_region
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_->
                        activity.callServiceDeleteRegion(listaRegiones!!, position)
                    }
                    .setNegativeButton("No") {_,_ ->
                        Toast.makeText(it.context,
                            "No se Elimino el Registro: " + listaRegiones!![position].id_region,
                            Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()

            }

            itemView.itemBtnEditarRegion.setOnClickListener {
                val position:Int = adapterPosition
                activity.enviar(it.context, listaRegiones, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterRegion.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_region, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterRegion.ViewHolder, position: Int) {
        holder.itemImagen.setImageResource(R.drawable.ic_region_sanitaria)
        holder.itemIdRegion.text = "Región Sanitaria No."+ listaRegiones!![position].id_region
        holder.itemDepartamento.text = "Departamento de " + listaRegiones!![position].departamento
        holder.itemJefatura.text = listaRegiones!![position].jefatura
        holder.itemTelefono.text = "Teléfono: " + listaRegiones!![position].telefono
    }

    override fun getItemCount(): Int {
        return listaRegiones!!.size
    }


}
