package com.rosariobagaskara.instock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class ProdukDataAdapter(private val produkList: ArrayList<ProdukData>): RecyclerView.Adapter<ProdukDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_produk,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = produkList[position]
        var itemListTemp = ""
        holder.produkName.text = currentItem.namaProduk
        for(value in currentItem.itemProduk){
            itemListTemp += "$value\n "
        }
        holder.produkItemList.text = itemListTemp
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val produkName : TextView = itemView.findViewById(R.id.namaProdukValueTextView)
        val produkItemList : TextView = itemView.findViewById(R.id.ProdukListItemValueTextView)
    }
}