package com.rosariobagaskara.instock

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class LogPenjualanStokAdapter(private val logStokList: ArrayList<LogStokData>): RecyclerView.Adapter<LogPenjualanStokAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_penjualan_stok, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = logStokList[position]
        if(position %2 == 0) {
            holder.penjualanStokCardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.penjualanStokCardView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }
        holder.namaStokRow.text = currentItem.logStokName
        holder.jumlahStokRow.text = currentItem.logStokQuantity.toString()
    }

    override fun getItemCount(): Int {
        return logStokList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val namaStokRow = itemView.findViewById<TextView>(R.id.namaStokTextViewRow)
        val jumlahStokRow = itemView.findViewById<TextView>(R.id.jumlahStokTextViewRow)
        val penjualanStokCardView = itemView.findViewById<CardView>(R.id.penjualanStokCardView)
    }
}