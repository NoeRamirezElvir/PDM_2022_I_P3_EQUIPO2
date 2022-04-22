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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerEstablecimientosActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EstablecimientoSaludDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_establecimientos.view.*

class RecyclerAdapterEstablecimiento(pListaEstablecimientos:List<EstablecimientoSaludDataCollectionItem>? = null,
                                     activity_: VerEstablecimientosActivity):
    RecyclerView.Adapter<RecyclerAdapterEstablecimiento.ViewHolder>() {
    var listaEstablecimientos:List<EstablecimientoSaludDataCollectionItem>? = pListaEstablecimientos
    var message:String = ""
    var activity: VerEstablecimientosActivity = activity_

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:ImageView = itemView.findViewById(R.id.itemImagenEstablecimientos)
        var itemIdEstablecimiento :TextView = itemView.findViewById(R.id.itemIdEstablecimiento)
        var itemNbeEstablecimiento:TextView = itemView.findViewById(R.id.itemNombreEstablecimiento)
        var itemDireccion:TextView = itemView.findViewById(R.id.itemDireccionEstablecimiento)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefonoEstablecimiento)
        var itemMunicipio:TextView = itemView.findViewById(R.id.itemMunicipioEstablecimiento)
        init {
            itemView.itemBtnBorrarEstablecimiento.setOnClickListener {
                val position:Int = adapterPosition
                message = "Establecimiento: " + listaEstablecimientos!![position].nombre +
                        "\nDireccion: " + listaEstablecimientos!![position].direccion +
                        "\nTelefono: " + listaEstablecimientos!![position].telefono +
                        "\nMunicipio No." + listaEstablecimientos!![position].id_municipio +
                        "\n ID: " + listaEstablecimientos!![position].id_municipio
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteEstablecimiento(listaEstablecimientos!!, position)
                    }
                    .setNegativeButton("No") {_,_ ->
                        Toast.makeText(it.context, "No se elimino el registro: " +
                                listaEstablecimientos!![position].id_establecimiento,
                            Toast.LENGTH_LONG).show()
                    }.create()
                dialog.show()
            }

            itemView.itemBtnEditarEstablecimiento.setOnClickListener {
                val position: Int = adapterPosition
                activity.enviar(it.context, listaEstablecimientos!!, position)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterEstablecimiento.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_establecimientos, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterEstablecimiento.ViewHolder, position: Int) {
        holder.itemImagen.setImageResource(R.drawable.ic_salud_establecimiento)
        holder.itemIdEstablecimiento.text = "Establecimiento de Salud No." +
                listaEstablecimientos!![position].id_establecimiento
        holder.itemNbeEstablecimiento.text = listaEstablecimientos!![position].nombre
        holder.itemDireccion.text = "Dirección: " + listaEstablecimientos!![position].direccion
        holder.itemTelefono.text = "Teléfono: " + listaEstablecimientos!![position].telefono
        holder.itemMunicipio.text = "Municipio No." + listaEstablecimientos!![position].id_municipio
    }

    override fun getItemCount(): Int {
        return listaEstablecimientos!!.size
    }


}
