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

class RecyclerAdapterCivil(pLista:ArrayList<Civil>? = null) : RecyclerView.Adapter<RecyclerAdapterCivil.ViewHolder>(){
    var listaCivil:ArrayList<Civil>? = pLista

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView = itemView.findViewById(R.id.itemImageCivil)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreCivil)
        var itemDni:TextView    = itemView.findViewById(R.id.itemDniCivil)
        var itemFecha:TextView = itemView.findViewById(R.id.itemFechaCivil)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefonoCivil)
        var itemCorreo:TextView   = itemView.findViewById(R.id.itemCorreoCivil)
        init {
            itemView.imbtBorrarCivilR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarCivilR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCivil.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_civil,parent,false)
        return ViewHolder(v)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCivil.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.ic_man)
        holder.itemNombre.text = "Carlos Godoy"
        holder.itemDni.text = "0802200204856"
        holder.itemFecha.text = "15/11/2001"
        holder.itemTelefono.text = "98795644"
        holder.itemCorreo.text = "carlos@ujcv.edu.hn"
    }
    override fun getItemCount(): Int {
        return 1
        //return listaCivil!!.size
    }
}