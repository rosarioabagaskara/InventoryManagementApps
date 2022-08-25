package com.rosariobagaskara.instock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class OrderDataAdapter(private val c : Context, private val orderList : ArrayList<OrderData>): RecyclerView.Adapter<OrderDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_order,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = orderList[position]
        holder.cancelOrder.setOnClickListener { view ->
            if(c is MainActivity){
                c.cancelOrder(currentItem)
            }
        }

        holder.orderNumber.text = "Order #${currentItem.orderId}"
        holder.dateOrder.text = currentItem.dateOrder
        holder.namaPemesan.text = "Nama Pemesan: ${currentItem.namaPemesan}"
        holder.statusOrder.text = currentItem.statusOrder
        if(holder.statusOrder.text == "Canceled"){
            holder.statusOrder.setTextColor(Color.parseColor("#FF0000"))
            holder.cancelOrder.visibility = View.GONE
        }else if(holder.statusOrder.text == "Success"){
            holder.statusOrder.setTextColor(Color.parseColor("#4CAF50"))
            holder.cancelOrder.visibility = View.VISIBLE
        }
        var produkTemp = ""
        var produkQuantityTemp = ""
        val JsonProdukHashMap = JSONObject(currentItem.orderProduk as Map<String, Map<String, String>>)
        val orderHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
        for (i in 0 until orderHashMap.size){
            val index = orderHashMap[i.toString()]
            if (index != null) {
                produkTemp += "${index.get("NamaProduk").toString()}\n"
                produkQuantityTemp += "${index.get("QuantityStok").toString()}\n"
            }
        }
        holder.produkTextView.text = produkTemp
        holder.produkQuantityTextView.text = produkQuantityTemp
    }

    override fun getItemCount(): Int {

        return orderList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val orderNumber: TextView = itemView.findViewById(R.id.orderNumberTextView)
        val dateOrder: TextView = itemView.findViewById(R.id.dateTextView)
        val namaPemesan: TextView = itemView.findViewById(R.id.namaPemesanTextView)
        val statusOrder: TextView = itemView.findViewById(R.id.statusOrderTextView)
        val produkTextView: TextView = itemView.findViewById(R.id.itemTextView)
        val produkQuantityTextView: TextView = itemView.findViewById(R.id.itemNumberTextView)
        val cancelOrder: ImageView = itemView.findViewById(R.id.listOrderCancel)
    }
}