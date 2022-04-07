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
import kotlinx.android.synthetic.main.card_layout_carnet_detalle.view.*

class RecyclerAdapterCarnetDetalle(pLista:ArrayList<CarnetDetalle>? = null) : RecyclerView.Adapter<RecyclerAdapterCarnetDetalle.ViewHolder>() {
    var listaCarnetDetalle:ArrayList<CarnetDetalle>? = pLista

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemIdDetalleD:TextView = itemView.findViewById(R.id.itemIdCarnetD)
        var itemFechaD:TextView = itemView.findViewById(R.id.itemFechaD)
        var itemDosisD:TextView = itemView.findViewById(R.id.itemDosisD)
        var itemNombreVacunadorD:TextView = itemView.findViewById(R.id.itemIdEmpleadoVacunoD)
        var itemObservacionD:TextView = itemView.findViewById(R.id.itemObservacionesD)

        init {
            itemView.itemBorrarDetalleD.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
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
        holder.itemIdDetalleD.text = "1"
        holder.itemFechaD.text = "6/4/2022"
        holder.itemDosisD.text = "Primera Dosis"
        holder.itemNombreVacunadorD.text = "Carlos "
        holder.itemObservacionD.text = "N/A"
    }

    override fun getItemCount(): Int {
        return 1
        //return listaCarnetDetalle!!.size
    }
}