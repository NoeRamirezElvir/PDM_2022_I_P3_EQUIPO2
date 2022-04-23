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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerEmpleadosActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.EmpleadosDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_empleado.view.*

class RecyclerAdapterEmpleado(var listaEmpleado:List<EmpleadosDataCollectionItem>? = null,var activity:VerEmpleadosActivity) :
    RecyclerView.Adapter<RecyclerAdapterEmpleado.ViewHolder>(){


    inner class ViewHolder(itemView: View,):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView = itemView.findViewById(R.id.itemImageEnfermero)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreEnfermero)
        var itemDni:TextView = itemView.findViewById(R.id.itemDniEnfermero)
        var itemCodigo:TextView = itemView.findViewById(R.id.itemCodigo)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefono)
        var itemCargo:TextView = itemView.findViewById(R.id.itemCargoEnfermero)

        init {
            itemView.imbtBorrarEmpleado.setOnClickListener {
                val position : Int = adapterPosition
                val message = "ID: ${listaEmpleado!![position].id_empleado} Codigo: ${listaEmpleado!![position].codigo}" +
                              "\nDNI: ${listaEmpleado!![position].dni}" +
                              "\nNombre: ${listaEmpleado!![position].nombre}."
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteEmpleado(listaEmpleado!!,position)
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaEmpleado!![position].id_empleado, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }

            itemView.imbtEditarEmpleado.setOnClickListener {
                val position : Int = adapterPosition
                activity.enviar(it.context,listaEmpleado,position)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerAdapterEmpleado.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_empleado,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterEmpleado.ViewHolder, position: Int) {
        if(!listaEmpleado.isNullOrEmpty()){
            holder.itemImage.setImageResource(R.drawable.ic_empleado_)
            holder.itemNombre.text = listaEmpleado!![position].nombre
            holder.itemDni.text = listaEmpleado!![position].dni
            holder.itemCodigo.text = listaEmpleado!![position].codigo.toString()
            holder.itemTelefono.text = listaEmpleado!![position].telefono.toString()
            holder.itemCargo.text = "ID Cargo: ${listaEmpleado!![position].id_cargo.toString()}"
        }
    }
    override fun getItemCount(): Int {
        return listaEmpleado!!.size
    }
}