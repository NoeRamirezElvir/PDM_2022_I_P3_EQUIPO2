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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerVacunasActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.VacunaDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_civil.view.*
import kotlinx.android.synthetic.main.card_layout_vacuna.view.*

class RecyclerAdapterVacuna(pLista:List<VacunaDataCollectionItem>? = null,  activity: VerVacunasActivity) : RecyclerView.Adapter<RecyclerAdapterVacuna.ViewHolder>(){
    var listaVacuna:List<VacunaDataCollectionItem>?  = pLista
    var message: String = ""
    var activity2 : VerVacunasActivity = activity

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var itemImage:ImageView = itemView.findViewById(R.id.itemImageVacuna)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreVacuna)
        var itemFabricante:TextView = itemView.findViewById(R.id.itemFabricanteVacuna)
        var itemFechaL:TextView = itemView.findViewById(R.id.itemFechaLlegadaVacuna)
        var itemFechaV:TextView = itemView.findViewById(R.id.itemFechaVencimientoVacuna)
        var itemNumeroLote:TextView = itemView.findViewById(R.id.itemNumeroLoteVacuna)

        init {
            itemView.imbtBorrarVacunaR.setOnClickListener {
                val position : Int = adapterPosition
                message = listaVacuna!![position].nombre + "\nLaboratorio: " + listaVacuna!![position].id_fabricante +
                        "\nFecha LLegada" + listaVacuna!![position].fechaLlegada +"\nFecha Fabricación: " + listaVacuna!![position].fechaFabricacion+
                        "\n ID:" + listaVacuna!![position].id_vacuna
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteVacuna(listaVacuna!!,position)
                        activity2.callServiceGetVacunas()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaVacuna!![position].id_vacuna, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.imbtEditarVacunaR.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaVacuna,position)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterVacuna.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_vacuna,parent,false)
        return ViewHolder(v)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterVacuna.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.ic_vacuna_r)
        holder.itemNombre.text = listaVacuna!![position].nombre
        holder.itemFabricante.text = "Fabricante: " + listaVacuna!![position].id_fabricante
        holder.itemNumeroLote.text = "Lote: " + listaVacuna!![position].numeroLote
        holder.itemFechaL.text     = "Llegada: " + listaVacuna!![position].fechaLlegada
        holder.itemFechaV.text     = "Vencimiento: "+ listaVacuna!![position].fechaVencimiento
    }
    override fun getItemCount(): Int {
        return listaVacuna!!.size
    }
}