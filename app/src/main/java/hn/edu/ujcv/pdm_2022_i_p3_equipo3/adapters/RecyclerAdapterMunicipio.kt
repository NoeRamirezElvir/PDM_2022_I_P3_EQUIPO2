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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Municipio
import kotlinx.android.synthetic.main.card_layout_centro_vacunacion.view.*
import kotlinx.android.synthetic.main.card_layout_municipio.view.*

class RecyclerAdapterMunicipio(pListaMunicipios:ArrayList<Municipio>? = null): RecyclerView.Adapter<RecyclerAdapterMunicipio.ViewHolder>() {
    var listaRegiones:ArrayList<Municipio>? = pListaMunicipios

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImagen:       ImageView = itemView.findViewById(R.id.itemImagenMunicipio)
        var itemIdMunicipio:  TextView = itemView.findViewById(R.id.itemIdMunicipio)
        var itemMunicipio:    TextView = itemView.findViewById(R.id.itemNombreMunicipio)
        var itemRegion:       TextView = itemView.findViewById(R.id.itemRegionS)

        init {
            itemView.itemBtnBorrarMunicipio.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarMunicipio.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterMunicipio.ViewHolder {
        val v=LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_municipio, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterMunicipio.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.icono_municipio)
        holder.itemIdMunicipio.text = "Municipio No.11"
        holder.itemMunicipio.text = "Municipio: Tatumbla"
        holder.itemRegion.text = "Region Sanitaria No.11"
    }

    override fun getItemCount(): Int {
        return 1
    }


}