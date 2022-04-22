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
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.VerCivilesActivity
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.entities.CivilDataCollectionItem
import kotlinx.android.synthetic.main.card_layout_civil.view.*

class RecyclerAdapterCivil(pLista:List<CivilDataCollectionItem>? = null,  activity: VerCivilesActivity) : RecyclerView.Adapter<RecyclerAdapterCivil.ViewHolder>(){
    var listaCivil:List<CivilDataCollectionItem>?  = pLista
    var message: String = ""
    var activity2 : VerCivilesActivity = activity

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        var itemImage:ImageView = itemView.findViewById(R.id.itemImageCivil)
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreCivil)
        var itemDni:TextView    = itemView.findViewById(R.id.itemDniCivil)
        var itemFecha:TextView = itemView.findViewById(R.id.itemFechaCivil)
        var itemTelefono:TextView = itemView.findViewById(R.id.itemTelefonoCivil)
        var itemdireccion:TextView   = itemView.findViewById(R.id.itemDireccionCivil)

        init {
            itemView.imbtBorrarCivilR.setOnClickListener {
                val position : Int = adapterPosition
                message = listaCivil!![position].nombre + "\n" + listaCivil!![position].dni +
                        "\n" + listaCivil!![position].telefono +"\n" + listaCivil!![position].direccion+
                        "\n ID:" + listaCivil!![position].id_civil
                val dialog = AlertDialog.Builder(it.context)
                    .setTitle("Desea Eliminar este Registro?")
                    .setMessage(message)
                    .setPositiveButton("Si") {_,_ ->
                        activity2.callServiceDeleteCivil(listaCivil!!,position)
                        activity2.callServiceGetCiviles()
                    }
                    .setNegativeButton("No"){_,_ ->
                        Toast.makeText(it.context, "No se Elimino el registro: " + listaCivil!![position].id_civil, Toast.LENGTH_SHORT).show()
                    }.create()
                dialog.show()
            }
            itemView.imbtEditarCivilR.setOnClickListener {
                val position : Int = adapterPosition
                activity2.enviar(it.context,listaCivil,position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterCivil.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_civil,parent,false)
        return ViewHolder(v)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterCivil.ViewHolder, position: Int) {
        holder.itemImage.setImageResource(R.drawable.img_ic_paciente)
        holder.itemNombre.text = listaCivil!![position].nombre
        holder.itemDni.text = "DNI: " + listaCivil!![position].dni
        holder.itemFecha.text = "Fecha Nac. : "+ listaCivil!![position].fechaNacimiento
        holder.itemTelefono.text = "Tel. : "+ listaCivil!![position].telefono
        holder.itemdireccion.text = "Dirección: "+ listaCivil!![position].direccion
    }
    override fun getItemCount(): Int {
        return listaCivil!!.size
    }

}