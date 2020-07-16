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
    var educadoresList: ArrayList<EducadorRemote>
) : RecyclerView.Adapter<EducadoresRVAdapter.EducadoresViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EducadoresViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.educadores_item, parent, false)
        return EducadoresViewHolder(itemView)
    }

    override fun getItemCount(): Int = educadoresList.size

    override fun onBindViewHolder(holder: EducadoresViewHolder, position: Int) {
        val educador = educadoresList[position]
        holder.bindEducador(educador)
    }

    class EducadoresViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        fun bindEducador(educador: EducadorRemote){
            itemView.tv_paridad.text = educador.nombre
            itemView.tv_fecha.text = educador.roll
            //Picasso.with(context).load("http://i.imgur.com/DvpvklR.png").into(imageView);
            if (!educador.foto.isNullOrEmpty())
                Picasso.get().load(educador.foto).into(itemView.iv_foto);
        }
    }
}