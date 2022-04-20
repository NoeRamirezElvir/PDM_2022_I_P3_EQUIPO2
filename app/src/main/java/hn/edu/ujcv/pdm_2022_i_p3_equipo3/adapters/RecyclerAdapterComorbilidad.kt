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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.ComorbilidadDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_comorbilidad.view.*

import kotlinx.android.synthetic.main.card_layout_region.view.*

class RecyclerAdapterComorbilidad(pListaComorbs:ArrayList<ComorbilidadDataCollectionItem>? = null): RecyclerView.Adapter<RecyclerAdapterComorbilidad.ViewHolder>() {
    var listaComorbs: ArrayList<ComorbilidadDataCollectionItem>? = pListaComorbs

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenComorb)
        var itemIdComorb: TextView = itemView.findViewById(R.id.itemIdComorbilidad)
        var itemNombreo: TextView = itemView.findViewById(R.id.itemNombreComorbilidad)
        var itemTipo: TextView = itemView.findViewById(R.id.itemTipoComorbilidad)

        init {
            itemView.itemBtnBorrarComorb.setOnClickListener {
                Toast.makeText(it.context, "Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarComorb.setOnClickListener {
                Toast.makeText(it.context, "Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterComorbilidad.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_comorbilidad, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterComorbilidad.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.img_cd_comorbilidad)
        holder.itemIdComorb.text = "Comorbilidad No.08"
        holder.itemNombreo.text = "Hipertensión Arterial"
        holder.itemTipo.text = "Medica Crónica"
    }

    override fun getItemCount(): Int {
        return 1
    }
}
