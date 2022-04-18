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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Cargo
import kotlinx.android.synthetic.main.card_layout_cargo.view.*

class RecyclerAdapterCargo(pListaCargo:ArrayList<Cargo>? = null) : RecyclerView.Adapter<RecyclerAdapterCargo.ViewHolder>() {
    var listaDocs: ArrayList<Cargo>? = pListaCargo

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImagen: ImageView = itemView.findViewById(R.id.itemImagenCargo)
        var itemIdCargo: TextView = itemView.findViewById(R.id.itemIdCargo)
        var itemNombreCargo: TextView = itemView.findViewById(R.id.itemNombreCargo)
        var itemDescripcionCargo: TextView = itemView.findViewById(R.id.itemDescripcionCargo)

        init {
            itemView.itemBtnBorrarCargo.setOnClickListener {
                Toast.makeText(it.context, "Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.itemBtnEditarCargo.setOnClickListener {
                Toast.makeText(it.context, "Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecyclerAdapterCargo.ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_layout_cargo, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCargo.ViewHolder, position: Int) {
        //val region=listaRegiones[position]
        holder.itemImagen.setImageResource(R.drawable.ic_no_image)
        holder.itemIdCargo.text = "Cargo: 5"
        holder.itemNombreCargo.text = "Enfermero"
        holder.itemDescripcionCargo.text = "Inyecciones"
    }

    override fun getItemCount(): Int {
        return 1
    }
}