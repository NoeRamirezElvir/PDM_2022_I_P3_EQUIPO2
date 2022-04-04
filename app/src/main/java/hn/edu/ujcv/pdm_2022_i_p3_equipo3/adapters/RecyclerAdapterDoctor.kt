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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Doctor
import kotlinx.android.synthetic.main.card_layout_comorbilidad.view.*
import kotlinx.android.synthetic.main.card_layout_doctor.view.*

class RecyclerAdapterDoctor(pListaDocs:ArrayList<Doctor>? = null) : RecyclerView.Adapter<RecyclerAdapterDoctor.ViewHolder>() {
    var listaDocs: ArrayList<Doctor>? = pListaDocs

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenDoc)
        var itemIdDoc: TextView = itemView.findViewById(R.id.itemIdDoctor)
        var itemNombreDoc: TextView = itemView.findViewById(R.id.itemNombreDoc)
        var itemDniDoc: TextView = itemView.findViewById(R.id.itemDniDoctor)
        var itemEspDoc: TextView = itemView.findViewById(R.id.itemEspecialidad)
        var itemTelefo: TextView = itemView.findViewById(R.id.itemTelefonoDoc)

        init {
            itemView.itemBtnBorrarDoc.setOnClickListener {
                Toast.makeText(it.context, "Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarDoc.setOnClickListener {
                Toast.makeText(it.context, "Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterDoctor.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_doctor, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterDoctor.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.ic_no_image)
        holder.itemIdDoc.text = "Doctor: 5"
        holder.itemNombreDoc.text = "Franz Beckenbauer "
        holder.itemDniDoc.text = "Dni: 0801200114523"
        holder.itemEspDoc.text = "Medico General"
        holder.itemTelefo.text = "Tel: 9867-4532"
    }

    override fun getItemCount(): Int {
        return 1
    }
}