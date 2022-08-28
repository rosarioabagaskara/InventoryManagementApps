package com.rosariobagaskara.instock

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class ProdukDataAdapter(private val c: Context, private val produkList: ArrayList<ProdukData>): RecyclerView.Adapter<ProdukDataAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_produk,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = produkList[position]
        var itemListTemp = ""
        val JsonProdukHashMap = JSONObject(currentItem.itemProduk as Map<String, Map<String, String>>)
        val produkHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
        for (i in 0 until produkHashMap.size){
            val index = produkHashMap[i.toString()]
            if (index != null) {
                itemListTemp += "${index.get("NamaStok").toString()} x ${index.get("QuantityStok")}\n"
            }
        }
        holder.listProdukCardView.setOnClickListener { view ->
            if(c is MainActivity){
                c.updateProduk(currentItem)
            }
        }
        holder.deleteImageView.setOnClickListener { view ->
            if(c is MainActivity){
                c.deleteProduk(currentItem)
            }
        }
        if(position %2 == 0) {
            holder.listProdukCardView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }else {
            holder.listProdukCardView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        holder.produkName.text = currentItem.namaProduk
        holder.produkItemList.text = itemListTemp
    }

    override fun getItemCount(): Int {
        return produkList.size
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val produkName : TextView = itemView.findViewById(R.id.namaProdukValueTextView)
        val produkItemList : TextView = itemView.findViewById(R.id.ProdukListItemValueTextView)
        val deleteImageView : ImageView = itemView.findViewById(R.id.listProdukButtonDelete)
        val listProdukCardView : CardView = itemView.findViewById(R.id.listProdukCardView)
    }
}