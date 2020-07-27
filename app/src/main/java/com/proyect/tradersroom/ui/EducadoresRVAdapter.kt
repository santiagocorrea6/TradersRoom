package com.proyect.tradersroom.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.EducadorRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.educadores_item.view.*

class EducadoresRVAdapter (
    var educadoresList: ArrayList<EducadorRemote>,
    private val itemClickListener: OnEducadorClickListener
) : RecyclerView.Adapter<EducadoresRVAdapter.EducadoresViewHolder>(){

    interface OnEducadorClickListener{
        fun onItemClick(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducadoresViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.educadores_item, parent, false)
        return EducadoresViewHolder(itemView)
    }

    override fun getItemCount(): Int = educadoresList.size

    override fun onBindViewHolder(holder: EducadoresViewHolder, position: Int) {
        val educador = educadoresList[position]
        holder.bindEducador(educador)
    }

    inner class EducadoresViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        fun bindEducador(educador: EducadorRemote){
            itemView.tv_paridad.text = educador.nombre
            itemView.tv_fecha_nacimiento.text = educador.roll
            if (!educador.foto.isNullOrEmpty())
                Picasso.get().load(educador.foto).into(itemView.iv_foto)

            itemView.setOnClickListener { itemClickListener.onItemClick("${educador.id}") }


        }
    }
}