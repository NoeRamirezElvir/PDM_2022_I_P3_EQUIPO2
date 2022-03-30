package hn.edu.ujcv.pdm_2022_i_p3_equipo3.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.R
import hn.edu.ujcv.pdm_2022_i_p3_equipo3.clases.Usuario
import kotlinx.android.synthetic.main.card_layout_usuario.view.*

class RecyclerAdapterUsuario (pLista:ArrayList<Usuario>? = null) : RecyclerView.Adapter<RecyclerAdapterUsuario.ViewHolder>(){
    var listaUsuario:ArrayList<Usuario>? = pLista

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var itemNombre:TextView = itemView.findViewById(R.id.itemNombreUsuario)
        var itemCorreo:TextView = itemView.findViewById(R.id.itemCorreoElectronicoUsuario)
        var itemContra:TextView = itemView.findViewById(R.id.itemContraUsuario)
        init {
            itemView.imbtBorrarUsuarioR.setOnClickListener {
                Toast.makeText(it.context,"Eliminado Correctamente", Toast.LENGTH_LONG).show()
            }
            itemView.imbtEditarUsuarioR.setOnClickListener {
                Toast.makeText(it.context,"Se abre el registro", Toast.LENGTH_LONG).show()
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterUsuario.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.card_layout_usuario,parent,false)
        return ViewHolder(v)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerAdapterUsuario.ViewHolder, position: Int) {
        holder.itemNombre.text = "Administrador"
        holder.itemContra.text = "Ujcv.2022"
        holder.itemCorreo.text = "admin@ujcv.edu.hn"
    }

    override fun getItemCount(): Int {
        return 1
        //return listaUsuario!!.size
    }
}