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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerMunicipiosActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.MunicipioDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_municipio.view.*

class RecyclerAdapterMunicipio(pListaMunicipios:List<MunicipioDataCollectionItem>? = null,
                               activity_: VerMunicipiosActivity):
    RecyclerView.Adapter<RecyclerAdapterMunicipio.ViewHolder>() {
    var listaMunicipios:List<MunicipioDataCollectionItem>? = pListaMunicipios
    var message:String = ""
    var activity: VerMunicipiosActivity = activity_

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:       ImageView = itemView.findViewById(R.id.itemImagenMunicipio)
        var itemIdMunicipio:  TextView = itemView.findViewById(R.id.itemIdMunicipio)
        var itemMunicipio:    TextView = itemView.findViewById(R.id.itemNombreMunicipio)
        var itemRegion:       TextView = itemView.findViewById(R.id.itemRegionS)

        init {
            itemView.itemBtnBorrarMunicipio.setOnClickListener {
                val position:Int = adapterPosition
                message = "Municipio: " + listaMunicipios!![position].nombre +
                        "\nRegion Sanitaria No: " + listaMunicipios!![position].id_region +
                        "\n ID: " + listaMunicipios!![position].id_municipio
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteMunicipio(listaMunicipios!!, position)
                    }
                    .setNegativeButton("No") {_,_ ->
                        Toast.makeText(it.context, "No se elimino el registro: " +
                                listaMunicipios!![position].id_municipio, Toast.LENGTH_LONG).show()
                    }.create()
                dialog.show()
            }

            itemView.itemBtnEditarMunicipio.setOnClickListener {
                val position: Int = adapterPosition
                activity.enviar(it.context, listaMunicipios, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterMunicipio.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_municipio, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterMunicipio.ViewHolder, position: Int) {
        holder.itemImagen.setImageResource(R.drawable.icono_municipio)
        holder.itemIdMunicipio.text = "Municipio No." + listaMunicipios!![position].id_municipio
        holder.itemMunicipio.text = "Municipio: " + listaMunicipios!![position].nombre
        holder.itemRegion.text = "Region Sanitaria No." + listaMunicipios!![position].id_region
    }

    override fun getItemCount(): Int {
        return listaMunicipios!!.size
    }


}
