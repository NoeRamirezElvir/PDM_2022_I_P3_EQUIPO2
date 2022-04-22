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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.RegistrarCargoActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCargosActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Cargo
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CargosDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_cargo.view.*

class RecyclerAdapterCargo(var listaCargo:List<CargosDataCollectionItem>? = null, var activity:VerCargosActivity) : RecyclerView.Adapter<RecyclerAdapterCargo.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenCargo)
        var itemIdCargo: TextView = itemView.findViewById(R.id.itemIdCargo)
        var itemNombreCargo: TextView = itemView.findViewById(R.id.itemNombreCargo)
        var itemDescripcionCargo: TextView = itemView.findViewById(R.id.itemDescripcionCargo)

        init {
            itemView.itemBtnBorrarCargo.setOnClickListener {
                val position : Int = adapterPosition
                val message = "ID: ${listaCargo!![position].id_cargo}" +
                        "\nNombre: ${listaCargo!![position].nombre}" +
                        "\nDescripcion: ${listaCargo!![position].descripcion}."
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteCargo(listaCargo!!,position)
                        activity.callServiceGetCargos()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaCargo!![position].id_cargo, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.itemBtnEditarCargo.setOnClickListener {
                val position : Int = adapterPosition
                activity.enviar(it.context,listaCargo,position)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterCargo.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_cargo, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCargo.ViewHolder, position: Int) {
        if(listaCargo!=null){
            holder.itemImagen.setImageResource(R.drawable.ic_cargo)
            holder.itemIdCargo.text = listaCargo!![position].id_cargo.toString()
            holder.itemNombreCargo.text = listaCargo!![position].nombre
            holder.itemDescripcionCargo.text = listaCargo!![position].descripcion
        }

    }

    override fun getItemCount(): Int {
       return listaCargo!!.size
    }
}