package com.proyect.tradersroom.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.proyect.tradersroom.R
import com.proyect.tradersroom.model.remote.BitacoraRemote
import kotlinx.android.synthetic.main.bitacoras_item.view.*
import kotlinx.android.synthetic.main.fragment_bitacora.*
import kotlin.math.absoluteValue

class BitacorasRVAdapter (
    var bitacorasList: ArrayList<BitacoraRemote>
) : RecyclerView.Adapter<BitacorasRVAdapter.BitacorasViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BitacorasViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.bitacoras_item, parent, false)
        return BitacorasViewHolder(itemView)
    }

    override fun getItemCount(): Int = bitacorasList.size

    override fun onBindViewHolder(holder: BitacorasViewHolder, position: Int) {
        val bitacora = bitacorasList[position]
        holder.bindBitacora(bitacora)
    }

    class BitacorasViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView){

        fun bindBitacora(bitacora: BitacoraRemote){
            if(bitacora.id == "0") {
                itemView.tv_id.text = "ID"
                itemView.tv_rentabilidad.text = "RENTABILIDAD"
                itemView.tv_paridad.text = "PARIDAD"
                itemView.tv_fecha_nacimiento.text = "FECHA"
                itemView.tv_ganancia.text = "GANANCIA"
                itemView.tv_inversion.text = "INVERSION"
                itemView.iv_buySell_up.setVisibility(View.VISIBLE)
                itemView.iv_buySell_down.setVisibility(View.GONE)
                itemView.iv_resultado_ganada.setVisibility(View.VISIBLE)
                itemView.iv_resultado_perdida.setVisibility(View.GONE)
            } else {
                itemView.tv_id.text = bitacora.id
                itemView.tv_rentabilidad.text = "${bitacora.rentabilidad.toString()}%"
                itemView.tv_paridad.text = bitacora.paridad
                itemView.tv_fecha_nacimiento.text = bitacora.fecha
                //itemView.tv_ganancia.text = "$${bitacora.ganancia.toString()}"
                itemView.tv_inversion.text = "$${bitacora.inversion.toString()}"

                //LOGICA PARA LA GANANCIA
                if (bitacora.ganancia.toFloat() < 0.0){
                    itemView.tv_ganancia.text = "-$${bitacora.ganancia.toFloat().absoluteValue.toString()}"
                    itemView.tv_ganancia.setTextColor(Color.parseColor("#FF0000"))
                } else {
                    itemView.tv_ganancia.text = "+$${bitacora.ganancia.toString()}"
                    itemView.tv_ganancia.setTextColor(Color.parseColor("#008000"))
                }

                //LOGICA ICONO GANADA O PERDIDA
                if (bitacora.resultado == "Ganada") {
                    itemView.iv_resultado_ganada.setVisibility(View.VISIBLE)
                    itemView.iv_resultado_perdida.setVisibility(View.GONE)
                } else {
                    itemView.iv_resultado_ganada.setVisibility(View.GONE)
                    itemView.iv_resultado_perdida.setVisibility(View.VISIBLE)
                }

                //LOGICA ICONO COMPRA O VENTA
                if (bitacora.buySell == "Buy") {
                    itemView.iv_buySell_up.setVisibility(View.VISIBLE)
                    itemView.iv_buySell_down.setVisibility(View.GONE)
                } else {
                    itemView.iv_buySell_up.setVisibility(View.GONE)
                    itemView.iv_buySell_down.setVisibility(View.VISIBLE)
                }
            }
        }
    }
}