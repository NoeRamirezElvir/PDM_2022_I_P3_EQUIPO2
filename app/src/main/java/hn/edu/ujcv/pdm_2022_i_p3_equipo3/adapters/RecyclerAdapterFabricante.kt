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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Comorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Fabricante
import kotlinx.android.synthetic.main.card_layout_comorbilidad.view.*
import kotlinx.android.synthetic.main.card_layout_fabricante.view.*

class RecyclerAdapterFabricante(pListaFabri:ArrayList<Fabricante>? = null): RecyclerView.Adapter<RecyclerAdapterFabricante.ViewHolder>() {
    var listaComorbs: ArrayList<Fabricante>? = pListaFabri

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenFabric)
        var itemNombreLab: TextView = itemView.findViewById(R.id.itemNombreLab)
        var itemIdLab: TextView = itemView.findViewById(R.id.itemIdLab)
        var itemPaisO: TextView = itemView.findViewById(R.id.itemPaisLab)
        var itemNombreCon: TextView = itemView.findViewById(R.id.itemNombreContacto)
        var itemTelContac: TextView = itemView.findViewById(R.id.itemTelefonoContac)

        init {
            itemView.itemBtnBorrarLab.setOnClickListener {
                Toast.makeText(it.context, "Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarLab.setOnClickListener {
                Toast.makeText(it.context, "Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterFabricante.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_fabricante, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterFabricante.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.img_cd_fabricante)
        holder.itemIdLab.text = "Laboratorio: 08"
        holder.itemNombreLab.text = "Sinovac"
        holder.itemPaisO.text = "País de Origen: China"
        holder.itemNombreCon.text = "Liu Peicheng"
        holder.itemTelContac.text = "+86 16533907254"
    }

    override fun getItemCount(): Int {
        return 1
    }
}