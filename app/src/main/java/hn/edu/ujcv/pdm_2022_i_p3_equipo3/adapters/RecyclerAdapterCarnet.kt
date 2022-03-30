package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CarnetVacunacion
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Civil
import kotlinx.android.synthetic.main.card_layout_carnet.view.*
import kotlinx.android.synthetic.main.card_layout_civil.view.*

class RecyclerAdapterCarnet(pLista:ArrayList<CarnetVacunacion>? = null) : RecyclerView.Adapter<RecyclerAdapterCarnet.ViewHolder>(){
    var listaCarnet:ArrayList<CarnetVacunacion>? = pLista
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreCivilCarnet)
        var itemCentroSalud:TextView = itemView.findViewById(R.id.itemNombreCentroSaludCarnet)
        var itemFecha:TextView = itemView.findViewById(R.id.itemFechaNacimientoCarnet)
        var itemNumeroLote:TextView = itemView.findViewById(R.id.itemNumeroLoteCarnet)
        var itemNumeroTelefono:TextView = itemView.findViewById(R.id.itemNumeroTelefonoCarnet)
        var itemTipoVacuna:TextView = itemView.findViewById(R.id.itemVacunaCarnet)
        var itemNombreVacunador:TextView = itemView.findViewById(R.id.itemVacunador)
        var itemDni:TextView = itemView.findViewById(R.id.itemDniCarnet)
        init {
            itemView.imbtBorrarCarnetR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarCarnetR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCarnet.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_carnet,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCarnet.ViewHolder, position: Int) {
        holder.itemNombre.text = "Alejandra Flores "
        holder.itemDni.text    = "DNI: 0896785999087"
        holder.itemCentroSalud.text = "Direccion: Cis. La Joya "
        holder.itemFecha.text = "Fecha de Nacimiento: 16/12/2000 "
        holder.itemNumeroLote.text = "Lote: 77889 "
        holder.itemNumeroTelefono.text = "Telefono: 22467889 "
        holder.itemTipoVacuna.text = "Aztrazeneca"
        holder.itemNombreVacunador.text = "Vacunador: Olvin Ramos"
    }

    override fun getItemCount(): Int {
       return 1
    // return listaCarnet!!.size
    }
}