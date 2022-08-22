package com.rosariobagaskara.instock

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList
import kotlin.coroutines.coroutineContext

class StockDataAdapter(private val c: Context, private val stockList: ArrayList<StockData>): RecyclerView.Adapter<StockDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_stock,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = stockList.get(position)

        holder.stockName.text = currentItem.stockName
        holder.stockQuantityValue.text = "${currentItem.stockQuantity} Pcs"
        holder.listStockCardView.setOnClickListener{view ->
            if(c is MainActivity){
                c.updateListStock(currentItem)
            }
        }

        holder.listStockButtonDelete.setOnClickListener{view ->
            if(c is MainActivity){
                c.deleteListStock(currentItem)
            }
        }

    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val stockName = itemView.findViewById<TextView>(R.id.namaStockValueTextView)
        val stockQuantityValue = itemView.findViewById<TextView>(R.id.stockQuantityValueTextView)
        val listStockCardView = itemView.findViewById<CardView>(R.id.listStockCardView)
        val listStockButtonDelete = itemView.findViewById<ImageView>(R.id.listStockButtonDelete)
    }
}