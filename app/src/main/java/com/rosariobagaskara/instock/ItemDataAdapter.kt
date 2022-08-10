package com.rosariobagaskara.instock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ItemDataAdapter(private val itemList: ArrayList<ItemData>): RecyclerView.Adapter<ItemDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = itemList[position]

        holder.itemName.text = currentItem.itemName
        holder.itemQuantity.text = currentItem.itemQuantity
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.namaItemValueTextView)
        val itemQuantity : TextView = itemView.findViewById(R.id.itemQuantityValueTextView)
    }
}