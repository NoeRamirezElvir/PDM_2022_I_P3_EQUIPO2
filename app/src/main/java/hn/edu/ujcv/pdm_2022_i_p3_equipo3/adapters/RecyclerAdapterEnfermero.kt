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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Enfermero
import kotlinx.android.synthetic.main.card_layout_empleado.view.*

class RecyclerAdapterEnfermero(pLista:ArrayList<Enfermero>? = null) : RecyclerView.Adapter<RecyclerAdapterEnfermero.ViewHolder>(){
    var listaEnfermero:ArrayList<Enfermero>? = pLista

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemImage:ImageView = itemView.findViewById(R.id.itemImageEnfermero)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreEnfermero)
        var itemDni:TextView = itemView.findViewById(R.id.itemDniEnfermero)
        var itemCodigo:TextView = itemView.findViewById(R.id.itemCodigo)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefono)
        var itemCargo:TextView = itemView.findViewById(R.id.itemCargoEnfermero)
        init {
            itemView.imbtBorrarEnfermeroR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente",Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarEnfermeroR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): RecyclerAdapterEnfermero.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_empleado,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterEnfermero.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.ic_no_image)
        holder.itemNombre.text = "Juan Ramiro"
        holder.itemDni.text = "0801262200482"
        holder.itemCodigo.text = "2019130158"
        holder.itemTelefono.text = "98546789"
        holder.itemCargo.text = "Supervisor"
    }

    override fun getItemCount(): Int {
        return 1
    //return listaEnfermero!!.size
    }
}