package com.rosariobagaskara.instock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class OrderDataAdapter(private val orderList : ArrayList<OrderData>): RecyclerView.Adapter<OrderDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_order,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = orderList[position]

        holder.orderNumber.text = currentItem.orderNumber
        holder.dateOrder.text = currentItem.dateOrder
        holder.namaPemesan.text = currentItem.namaPemesan
        holder.item.text = currentItem.item
        holder.itemNumber.text = currentItem.itemNumber

    }

    override fun getItemCount(): Int {

        return orderList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val orderNumber: TextView = itemView.findViewById(R.id.orderNumberTextView)
        val dateOrder: TextView = itemView.findViewById(R.id.dateTextView)
        val namaPemesan: TextView = itemView.findViewById(R.id.namaPemesanTextView)
        val item: TextView = itemView.findViewById(R.id.itemTextView)
        val itemNumber: TextView = itemView.findViewById(R.id.itemNumberTextView)
    }
}