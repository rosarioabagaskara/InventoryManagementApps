package com.rosariobagaskara.instock

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class PendapatanLaundryAdapter(private val orderList: ArrayList<OrderData>): RecyclerView.Adapter<PendapatanLaundryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_pendapatan_laundry, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = orderList[position]
        holder.orderNumberRow.text = "Order #${currentItem.orderId}"
        val JsonProdukHashMap = JSONObject(currentItem.orderProduk as Map<String, Map<String, String>>)
        val orderHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
        var produkTemp = ""
        var produkQuantityTemp = ""
        var hargaTemp = ""
        for (i in 0 until orderHashMap.size){
            val index = orderHashMap[i.toString()]
            if (index != null) {
                if(index["JenisProduk"] == "Laundry"){
                    holder.pendapatanCardView.visibility = View.VISIBLE
                    val formatter: NumberFormat = DecimalFormat("#,###")
                    val myNumber = index.get("HargaProduk")?.toDouble()
                    val formattedNumber = formatter.format(myNumber)
                    produkTemp += "${index.get("NamaProduk").toString()}\n"
                    produkQuantityTemp += "${index.get("QuantityProduk").toString()}\n"
                    hargaTemp += "Rp. ${formattedNumber}\n"
                }
            }
        }
        if(position %2 == 0) {
            holder.pendapatanCardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.pendapatanCardView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        holder.namaProdukRow.text = produkTemp
        holder.jumlahProdukRow.text = produkQuantityTemp
        holder.subtotalRow.text = hargaTemp
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val orderNumberRow = itemView.findViewById<TextView>(R.id.orderNumberLaundryTextViewRow)
        val namaProdukRow = itemView.findViewById<TextView>(R.id.namaProdukLaundryTextViewRow)
        val jumlahProdukRow = itemView.findViewById<TextView>(R.id.jumlahProdukLaundryTextViewRow)
        val subtotalRow = itemView.findViewById<TextView>(R.id.subtotalLaundryTextViewRow)
        val pendapatanCardView = itemView.findViewById<CardView>(R.id.pendapatanLaundryCardView)
    }
}