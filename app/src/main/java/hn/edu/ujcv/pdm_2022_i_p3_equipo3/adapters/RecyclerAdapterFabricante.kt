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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCivilesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerFabricantesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Comorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Fabricante
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.FabricanteDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_comorbilidad.view.*
import kotlinx.android.synthetic.main.card_layout_fabricante.view.*

class RecyclerAdapterFabricante(pListaFabri:List<FabricanteDataCollectionItem>? = null,activity:VerFabricantesActivity): RecyclerView.Adapter<RecyclerAdapterFabricante.ViewHolder>() {
    var listaFabs: List<FabricanteDataCollectionItem>? = pListaFabri
    var message: String = ""
    var activity2 : VerFabricantesActivity = activity

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenFabric)
        var itemNombreLab: TextView = itemView.findViewById(R.id.itemNombreLab)
        var itemIdLab: TextView = itemView.findViewById(R.id.itemIdLab)
        var itemPaisO: TextView = itemView.findViewById(R.id.itemPaisLab)
        var itemNombreCon: TextView = itemView.findViewById(R.id.itemNombreContacto)
        var itemTelContac: TextView = itemView.findViewById(R.id.itemTelefonoContac)

        init {
            itemView.itemBtnBorrarLab.setOnClickListener {
                val position : Int = adapterPosition
                message = listaFabs!![position].laboratorio + "\nContacto:"  + listaFabs!![position].nombre_contacto +
                        "\nTel." + listaFabs!![position].telefono_contacto +
                        "\nID:" + listaFabs!![position].id_fabricante
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteFab(listaFabs!!,position)
                        activity2.callServiceGetFabs()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaFabs!![position].id_fabricante, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.itemBtnEditarLab.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaFabs,position)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterFabricante.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_fabricante, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterFabricante.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.img_cd_fabricante)
        holder.itemIdLab.text = "Laboratorio: "+ listaFabs!![position].id_fabricante
        holder.itemNombreLab.text = listaFabs!![position].laboratorio
        holder.itemPaisO.text = "País de Origen: "+listaFabs!![position].id_pais
        holder.itemNombreCon.text = "Contacto: "+ listaFabs!![position].nombre_contacto
        holder.itemTelContac.text = "Tel. : "+ listaFabs!![position].telefono_contacto
    }

    override fun getItemCount(): Int {
        return listaFabs!!.size
    }
}