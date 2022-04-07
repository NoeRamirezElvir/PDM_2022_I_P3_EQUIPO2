package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CarnetDetalle
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Paises
import kotlinx.android.synthetic.main.card_layout_carnet_detalle.view.*
import kotlinx.android.synthetic.main.card_layout_paises.view.*

class RecyclerAdapterPaises(pLista:ArrayList<Paises>? = null) : RecyclerView.Adapter<RecyclerAdapterPaises.ViewHolder>() {
    var listaPaises:ArrayList<Paises>? = pLista

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemIdpaisP: TextView = itemView.findViewById(R.id.itemIdPais)
        var itemNombreP: TextView = itemView.findViewById(R.id.itemNombrePais)
        var itemCodigoArea: TextView = itemView.findViewById(R.id.itemCodigoAreaPais)
        var itemCodigoIso: TextView = itemView.findViewById(R.id.itemCodigoIsoP)


        init {
            itemView.imbtBorrarPais.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarPais.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterPaises.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_paises,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterPaises.ViewHolder, position: Int) {
        holder.itemIdpaisP.text = "67"
        holder.itemNombreP.text= "Fernando Flores"
        holder.itemCodigoArea.text = "1"
        holder.itemCodigoArea.text=  "+504"
        holder.itemCodigoIso.text = "567788"
    }
    override fun getItemCount(): Int {
        return 1
        //return lista!!.size
    }
}