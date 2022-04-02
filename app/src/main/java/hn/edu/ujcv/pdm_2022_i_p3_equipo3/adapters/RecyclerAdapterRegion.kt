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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.RegionSanitaria
import kotlinx.android.synthetic.main.card_layout_centro_vacunacion.view.*
import kotlinx.android.synthetic.main.card_layout_region.view.*

class RecyclerAdapterRegion(pListaRegiones:ArrayList<RegionSanitaria>? = null): RecyclerView.Adapter<RecyclerAdapterRegion.ViewHolder>() {
    var listaRegiones:ArrayList<RegionSanitaria>? = pListaRegiones

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:       ImageView = itemView.findViewById(R.id.itemImagenRegion)
        var itemIdRegion:     TextView = itemView.findViewById(R.id.itemIdRegion)
        var itemDepartamento: TextView = itemView.findViewById(R.id.itemDepartamento)
        var itemJefatura:     TextView = itemView.findViewById(R.id.itemJefaturaRegional)
        var itemTelefono:     TextView = itemView.findViewById(R.id.itemTelefonoRegion)
        init {
            itemView.itemBtnBorrarRegion.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarRegion.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterRegion.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_region, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterRegion.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.ic_region_sanitaria)
        holder.itemIdRegion.text = "Región Sanitaria No.11"
        holder.itemDepartamento.text = "Departamento de Choluteca"
        holder.itemJefatura.text = "Sebastián Vallejo"
        holder.itemTelefono.text = "Teléfono: 2444-3603"
    }

    override fun getItemCount(): Int {
        return 1
    }


}