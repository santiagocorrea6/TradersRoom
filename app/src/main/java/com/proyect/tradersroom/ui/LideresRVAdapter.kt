package com.proyect.tradersroom.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.LiderRemote
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.lideres_item.view.*

class LideresRVAdapter (
    var lideresList: ArrayList<LiderRemote>,
    private val itemClickListener: LideresRVAdapter.OnLiderClickListener
) : RecyclerView.Adapter<LideresRVAdapter.LideresViewHolder>(){

    interface OnLiderClickListener{
        fun onItemClick(id: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LideresViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.lideres_item, parent, false)
        return LideresViewHolder(itemView)
    }

    override fun getItemCount(): Int = lideresList.size

    override fun onBindViewHolder(holder: LideresRVAdapter.LideresViewHolder, position: Int) {
        val lider = lideresList[position]
        holder.bindLider(lider)
    }

    inner class LideresViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        fun bindLider(lider: LiderRemote){
            itemView.tv_paridad.text = lider.nombre
            itemView.tv_fecha_nacimiento.text = lider.rango

            Picasso.get().load(lider.foto).into(itemView.iv_foto);

            itemView.setOnClickListener { itemClickListener.onItemClick("${lider.id}") }
        }
    }
}