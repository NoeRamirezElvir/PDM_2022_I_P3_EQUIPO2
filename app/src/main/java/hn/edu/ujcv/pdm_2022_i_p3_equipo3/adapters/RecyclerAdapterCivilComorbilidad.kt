package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCivilComorbilidadActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCivilesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.CivilComorbilidad
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilComorbilidadDataCollectionItem
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_civil_comorbilidad.view.*

class RecyclerAdapterCivilComorbilidad(pListaCivilComorbilidad:List<CivilComorbilidadDataCollectionItem>? = null, activity: VerCivilComorbilidadActivity) :
    RecyclerView.Adapter<RecyclerAdapterCivilComorbilidad.ViewHolder>(){
    var listaCivilComorb:List<CivilComorbilidadDataCollectionItem>?  = pListaCivilComorbilidad
    var message: String = ""
    var activity2 : VerCivilComorbilidadActivity = activity

    inner class ViewHolder (itemView: View):RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView = itemView.findViewById(R.id.itemImagenCivilComorb)
        var itemIdCivComorb : TextView = itemView.findViewById(R.id.itemIdCivilComorb)
        var itemCivil: TextView = itemView.findViewById(R.id.itemCivilComorb)
        var itemComorb: TextView = itemView.findViewById(R.id.itemComorb_civil)
        var itemEstado: TextView = itemView.findViewById(R.id.itemEstado)
        var itemObservacion: TextView = itemView.findViewById(R.id.itemObservacionComorb)
        init {
            itemView.itemBtnBorrarCivilComorb.setOnClickListener {
                val position : Int = adapterPosition
                message = "Paciente: "+listaCivilComorb!![position].id_civil + "\nComorbilidad:" + listaCivilComorb!![position].id_comorbilidad +
                        "\nEstado: " + listaCivilComorb!![position].estado +"\nObservacion: " + listaCivilComorb!![position].observacion+
                        "\n ID:" + listaCivilComorb!![position].id_civilComorbilidad
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteCivilComorb(listaCivilComorb!!,position)
                        activity2.callServiceGetCivilesComorbs()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaCivilComorb!![position].id_civilComorbilidad, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.itemBtnEditarCivilComorb.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaCivilComorb,position)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCivilComorbilidad.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_civil_comorbilidad,parent,false)
        return ViewHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCivilComorbilidad.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.img_civilcomorb)
        holder.itemIdCivComorb.text = "ID: "+listaCivilComorb!![position].id_civilComorbilidad
        holder.itemCivil.text = "Paciente: "+ listaCivilComorb!![position].id_civil
        holder.itemComorb.text = "Comorbilidad: " + listaCivilComorb!![position].id_comorbilidad
        holder.itemEstado.text = "Estado: " + listaCivilComorb!![position].estado
        holder.itemObservacion.text = "Observacion: "+ listaCivilComorb!![position].observacion
    }

    override fun getItemCount(): Int {
        return listaCivilComorb!!.size
    }

}