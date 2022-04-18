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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CivilComorbilidad
import kotlinx.android.synthetic.main.card_layout_civil_comorbilidad.view.*

class RecyclerAdapterCivilComorbilidad(pListaCivilComorbilidad:ArrayList<CivilComorbilidad>? = null) :
    RecyclerView.Adapter<RecyclerAdapterCivilComorbilidad.ViewHolder>(){

    inner class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.itemImagenCivilComorb)
        var itemIdCivComorb : TextView = itemView.findViewById(R.id.itemIdCivilComorb)
        var itemCivil: TextView = itemView.findViewById(R.id.itemCivilComorb)
        var itemComorb: TextView = itemView.findViewById(R.id.itemComorb_civil)
        var itemEstado: TextView = itemView.findViewById(R.id.itemEstado)
        var itemObservacion: TextView = itemView.findViewById(R.id.itemObservacionComorb)
        init {
            itemView.itemBtnBorrarCivilComorb.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarCivilComorb.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCivilComorbilidad.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_civil_comorbilidad,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCivilComorbilidad.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.img_civilcomorb)
        holder.itemIdCivComorb.text = "ID: 1"
        holder.itemCivil.text = "Paciente: Carlos Godoy"
        holder.itemComorb.text = "Comorbilidad: Diabetes"
        holder.itemEstado.text = "Estado: leve"
        holder.itemObservacion.text = "Observacion: N/A"
    }

    override fun getItemCount(): Int {
        return 1
    }

}