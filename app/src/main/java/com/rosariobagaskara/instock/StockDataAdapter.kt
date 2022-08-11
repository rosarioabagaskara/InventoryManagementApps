package com.rosariobagaskara.instock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

class StockDataAdapter(private val stockList: ArrayList<StockData>): RecyclerView.Adapter<StockDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_stock,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = stockList[position]

        holder.stockName.text = currentItem.stockName
        holder.stockQuantityValue.text = "${currentItem.stockQuantity} Pcs"

    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stockName : TextView = itemView.findViewById(R.id.namaStockValueTextView)
        val stockQuantityValue : TextView = itemView.findViewById(R.id.stockQuantityValueTextView)
    }
}