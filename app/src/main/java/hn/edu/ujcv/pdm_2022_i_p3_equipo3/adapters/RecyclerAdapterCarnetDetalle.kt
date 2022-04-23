package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.RegistrarCarnetActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCarnetDetalleActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCarnetMasDetalleActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetDetallesDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_carnet_detalle.view.*

class RecyclerAdapterCarnetDetalle(var lista:ArrayList<CarnetDetallesDataCollectionItem>? = null,
var activity:VerCarnetDetalleActivity?=null,var activity2:VerCarnetMasDetalleActivity?=null,var activity3:RegistrarCarnetActivity?=null) : RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>() {

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemIdDetalleD:TextView = itemView.findViewById(R.id.itemIdCarnetD)
        var itemFechaD:TextView = itemView.findViewById(R.id.itemFechaD)
        var itemDosisD:TextView = itemView.findViewById(R.id.itemDosisD)
        var itemNombreVacunadorD:TextView = itemView.findViewById(R.id.itemIdEmpleadoVacunoD)
        var itemObservacionD:TextView = itemView.findViewById(R.id.itemObservacionesD)

        init {
            itemView.itemBorrarDetalleD.setOnClickListener {
                val position : Int = adapterPosition
                val message = "ID del Carnet: ${lista!![position].id_carnetEncabezado}" +
                        "\nNumero de Carnet: ${lista!![position].observacion}."
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        if(activity!=null){
                            activity!!.callServiceDeleteCarnetDetalle(lista!!,position)
                        }
                        if(activity2!=null){
                            activity2!!.callServiceDeleteCarnetDetalle(lista!!,position)
                        }
                        if(activity3!=null){
                            activity3!!.callServiceDeleteCarnetDetalle(lista!!,position)
                        }
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + lista!![position].id_carnetEncabezado, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterCarnetDetalle.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_carnet_detalle,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCarnetDetalle.ViewHolder, position: Int) {
        if(lista != null){
            holder.itemIdDetalleD.text = "ID Detalle: ${lista!![position].id_carnetDetalle.toString()}"
            holder.itemFechaD.text =  lista!![position].fecha
            holder.itemDosisD.text =  lista!![position].dosis
            holder.itemNombreVacunadorD.text =  "ID vacunador: ${lista!![position].idEmpleadoVacuno}"
            holder.itemObservacionD.text =  lista!![position].observacion
        }
    }

    override fun getItemCount(): Int {
        return if(lista != null){
            lista!!.size
        }else{
            0
        }
    }
}