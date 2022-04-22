package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.RegistrarCarnetActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCarnetVacunacionActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CarnetVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Civil
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CarnetEncabezadoDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_carnet.view.*
import kotlinx.android.synthetic.main.card_layout_civil.view.*

class RecyclerAdapterCarnet(var lista:List<CarnetEncabezadoDataCollectionItem>? = null, var activity:VerCarnetVacunacionActivity) : RecyclerView.Adapter<RecyclerAdapterCarnet.ViewHolder>(){
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemNumeroC:TextView = itemView.findViewById(R.id.itemNumeroCarnet)
        var itemNombreC:TextView = itemView.findViewById(R.id.itemNombreCivilC)
        var itemIdC:TextView = itemView.findViewById(R.id.itemIdCarnetC)

        init {
            itemView.imbtBorrarCarnetR.setOnClickListener {
                val position : Int = adapterPosition
                val message = "ID del Carnet: ${lista!![position].id_carnetEncabezado}" +
                        "\nNumero de Carnet: ${lista!![position].numeroCarnet}."
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("¿Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity.callServiceDeleteCarnetEncabezado(lista!!,position)
                        activity.callServiceGetCarnetEncabezado()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + lista!![position].id_carnetEncabezado, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.imbtEditarCarnetR.setOnClickListener {
                val position : Int = adapterPosition
                activity!!.enviar(it.context,lista,position)
            }
            itemView.setOnClickListener{
                val position : Int = adapterPosition
                activity.abrirCarnetDetalles(it.context,lista,position)

            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCarnet.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_carnet,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCarnet.ViewHolder, position: Int) {
        if(lista!=null){
            holder.itemNumeroC.text = "No. ${lista!![position].numeroCarnet}"
            holder.itemNombreC.text    = "Paciente: ${lista!![position].id_civil}"
            holder.itemIdC.text = "ID Carnet: ${lista!![position].id_carnetEncabezado}"
        }
    }
    override fun getItemCount(): Int {
        return lista!!.size
    }
}