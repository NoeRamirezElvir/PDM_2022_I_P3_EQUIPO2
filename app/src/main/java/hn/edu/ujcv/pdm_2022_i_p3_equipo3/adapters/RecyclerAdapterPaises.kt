package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerPaisesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.PaisesDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_paises.view.*

class RecyclerAdapterPaises(pLista:List<PaisesDataCollectionItem>? = null,
                            activity_: VerPaisesActivity) : RecyclerView.Adapter<RecyclerAdapterPaises.ViewHolder>() {
    var listaPaises:List<PaisesDataCollectionItem>? = pLista
    var message:String = ""
    var activity:VerPaisesActivity = activity_

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemIdpaisP: TextView = itemView.findViewById(R.id.itemIdPais)
        var itemNombreP: TextView = itemView.findViewById(R.id.itemNombrePais)
        var itemCodigoArea: TextView = itemView.findViewById(R.id.itemCodigoAreaPais)
        var itemCodigoIso: TextView = itemView.findViewById(R.id.itemCodigoIsoP)


        init {
            itemView.imbtBorrarPais.setOnClickListener {
                val position:Int = adapterPosition
                message = "Pais: " + listaPaises!![position].nombre +
                        "\nCodigo de area: " + listaPaises!![position].codigo_area +
                        "\nCodigo ISO: " + listaPaises!![position].codigo_iso +
                        "\n ID: " + listaPaises!![position].id_pais
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeletePais(listaPaises!!, position)
                    }
                    .setNegativeButton("No") {_,_ ->
                        Toast.makeText(it.context, "No se elimino el registro: " +
                                listaPaises!![position].id_pais, Toast.LENGTH_LONG).show()
                    }.create()
                dialog.show()
            }

            itemView.imbtEditarPais.setOnClickListener {
                val position: Int = adapterPosition
                activity.enviar(it.context, listaPaises, position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterPaises.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_paises,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterPaises.ViewHolder, position: Int) {
        holder.itemIdpaisP.text = "ID: " + listaPaises!![position].id_pais
        holder.itemNombreP.text= listaPaises!![position].nombre
        holder.itemCodigoArea.text=  listaPaises!![position].codigo_area
        holder.itemCodigoIso.text = listaPaises!![position].codigo_iso
    }
    override fun getItemCount(): Int {
        return listaPaises!!.size
    }
}
