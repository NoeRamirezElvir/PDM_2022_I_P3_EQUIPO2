package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.RegistrarCarnetActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCarnetVacunacionActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CarnetVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Civil
import kotlinx.android.synthetic.main.card_layout_carnet.view.*
import kotlinx.android.synthetic.main.card_layout_civil.view.*

class RecyclerAdapterCarnet(pLista:ArrayList<CarnetVacunacion>? = null, var activity:VerCarnetVacunacionActivity? = null) : RecyclerView.Adapter<RecyclerAdapterCarnet.ViewHolder>(){
    var listaCarnet:ArrayList<CarnetVacunacion>? = pLista
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemNumeroC:TextView = itemView.findViewById(R.id.itemNumeroCarnet)
        var itemNombreC:TextView = itemView.findViewById(R.id.itemNombreCivilC)
        var itemIdC:TextView = itemView.findViewById(R.id.itemIdCarnetC)

        init {

            itemView.imbtBorrarCarnetR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarCarnetR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
            itemView.setOnClickListener{
                if(activity != null){
                    activity!!.abrirCarnetDetalles()
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCarnet.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_carnet,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCarnet.ViewHolder, position: Int) {
        holder.itemNumeroC.text = "No. 175466"
        holder.itemNombreC.text    = "Paciente: Alejandra Valle"
        holder.itemIdC.text = "ID Carnet: 1"

    }

    override fun getItemCount(): Int {
       return 1
    // return listaCarnet!!.size
    }
}