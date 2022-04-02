package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.EstablecimientoSalud
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RegionSanitaria
import kotlinx.android.synthetic.main.card_layout_centro_vacunacion.view.*
import kotlinx.android.synthetic.main.card_layout_establecimientos.view.*
import org.w3c.dom.Text

class RecyclerAdapterEstablecimiento(pListaEstablecimientos:ArrayList<EstablecimientoSalud>? = null): RecyclerView.Adapter<RecyclerAdapterEstablecimiento.ViewHolder>() {
    var listaEstablecimientos:ArrayList<EstablecimientoSalud>? = pListaEstablecimientos

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:ImageView = itemView.findViewById(R.id.itemImagenEstablecimientos)
        var itemIdEstablecimiento :TextView = itemView.findViewById(R.id.itemIdEstablecimiento)
        var itemNbeEstablecimiento:TextView = itemView.findViewById(R.id.itemNombreEstablecimiento)
        var itemDireccion:TextView = itemView.findViewById(R.id.itemDireccionEstablecimiento)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefonoEstablecimiento)
        var itemMunicipio:TextView = itemView.findViewById(R.id.itemMunicipioEstablecimiento)
        init {
            itemView.itemBtnBorrarEstablecimiento.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarEstablecimiento.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterEstablecimiento.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_establecimientos, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterEstablecimiento.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.ic_salud_establecimiento)
        holder.itemIdEstablecimiento.text = "Establecimiento de Salud No.4"
        holder.itemNbeEstablecimiento.text = "San Antonio de Oriente"
        holder.itemDireccion.text = "Dirección: Aldea El Terreo"
        holder.itemTelefono.text = "Teléfono: 27762374"
        holder.itemMunicipio.text = "Municipio de San Antonio"
    }

    override fun getItemCount(): Int {
        return 1
    }


}