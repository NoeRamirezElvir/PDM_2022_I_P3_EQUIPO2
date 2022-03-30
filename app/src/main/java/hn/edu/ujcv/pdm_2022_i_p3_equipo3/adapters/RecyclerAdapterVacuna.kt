package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Civil
import kotlinx.android.synthetic.main.card_layout_civil.view.*
import kotlinx.android.synthetic.main.card_layout_vacuna.view.*

class RecyclerAdapterVacuna(pLista:ArrayList<Civil>? = null) : RecyclerView.Adapter<RecyclerAdapterVacuna.ViewHolder>(){
    var listaCivil:ArrayList<Civil>? = pLista
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView = itemView.findViewById(R.id.itemImageVacuna)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreVacuna)
        var itemFabricante:TextView = itemView.findViewById(R.id.itemFabricanteVacuna)
        var itemFechaF:TextView = itemView.findViewById(R.id.itemFechaFabricacionVacuna)
        var itemFechaV:TextView = itemView.findViewById(R.id.itemFechaVencimientoVacuna)
        var itemNumeroLote:TextView = itemView.findViewById(R.id.itemNumeroLoteVacuna)

        init {
            itemView.imbtBorrarVacunaR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarVacunaR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterVacuna.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_vacuna,parent,false)
        return ViewHolder(v)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterVacuna.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.ic_no_image)
        holder.itemNombre.text = "Pfizer"
        holder.itemFabricante.text = "BioNTech, Fosun Pharma, Pfizer"
        holder.itemNumeroLote.text = "77803"
        holder.itemFechaF.text     = "12/10/2021"
        holder.itemFechaV.text     = "12/10/2023"
    }
    override fun getItemCount(): Int {
        return 1
        //return plista!!.size
    }
}