package com.rosariobagaskara.instock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class UpdateProdukActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_produk)
        updateRecord()


    }

    private fun updateRecord(){
        val updateNamaProduk = findViewById<EditText>(R.id.updateProdukNameValue)
        val updateLiterGalon = findViewById<EditText>(R.id.updateEditTextLiterPerGalon)
        val updateLinearLayout = findViewById<LinearLayout>(R.id.updateLayoutListProduk)
        val jenisProdukTextView = findViewById<TextView>(R.id.jenisProdukValue)
        val literGalonTextView = findViewById<TextView>(R.id.literGalon)
        val updateHarga = findViewById<EditText>(R.id.updateMataUangValue)
        val addItemBtn = findViewById<Button>(R.id.updateTambahItemProduk)
        val updateBtn = findViewById<Button>(R.id.updateProduk)
        val namaProduk = intent.getStringExtra("produkName")
        val jenisProduk = intent.getStringExtra("jenisProduk")
        val galonLiter = intent.getIntExtra("galonLiterValue",0)
        val itemProduk = intent.getStringExtra("itemProduk")
        val hargaProduk = intent.getDoubleExtra("harga",0.0).toString()

        jenisProdukTextView.setText("Jenis Produk: $jenisProduk")
        updateNamaProduk.setText(namaProduk.toString())
        if(jenisProduk == "Galon"){
            literGalonTextView.visibility = View.VISIBLE
            updateLiterGalon.visibility = View.VISIBLE
            updateLiterGalon.setText(galonLiter.toString())
        }else if(jenisProduk != "Galon"){
            literGalonTextView.visibility = View.GONE
            updateLiterGalon.visibility = View.GONE
            updateLiterGalon.setText("0")
        }
//            val JsonProdukHashMap = JSONObject(itemProduk as Map<String, Map<String, String>>)
//            val produkHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
//            for (i in 0 until produkHashMap.size){
//                val index = produkHashMap[i.toString()]
//
//            }
        updateHarga.setText(hargaProduk)

        addItemBtn.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val itemStokList : ArrayList<StockData> = databaseHandler.viewItemStock()
            val listAddItem = layoutInflater.inflate(R.layout.list_produk_add_item, null, false)
            val spinnerProdukItem : Spinner = listAddItem.findViewById(R.id.spinnerAddProdukItem)
            val editJumlahAddProdukItem : EditText = listAddItem.findViewById(R.id.editJumlahAddProdukItem)
            val hapusProdukItem : ImageView = listAddItem.findViewById(R.id.hapusProdukItem)

            spinnerProdukItem.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,itemStokList)

            hapusProdukItem.setOnClickListener {
                updateLinearLayout.removeView(listAddItem)
            }

            updateLinearLayout.addView(listAddItem)
        }



        updateBtn.setOnClickListener {
            val namaProduk : String = updateNamaProduk.text.toString()
            val galonLiter : Int = Integer.parseInt(updateLiterGalon.text.toString())
            val harga : String = updateHarga.text.toString()
            var produkHashMapItem : HashMap<String,String> = HashMap<String,String>()
            val produkHashMap : HashMap<String,HashMap<String,String>> = HashMap<String,HashMap<String,String>>()
            val layoutCount = updateLinearLayout.childCount
            for(i in 0 until layoutCount){
                val child = updateLinearLayout.getChildAt(i)
                val itemStokSpinner = child.findViewById<Spinner>(R.id.spinnerAddProdukItem)
                val itemStokQuantityTextView = child.findViewById<EditText>(R.id.editJumlahAddProdukItem)
                val selectedObject = itemStokSpinner.selectedItem as StockData
                produkHashMapItem = hashMapOf("ID" to selectedObject.stockId.toString(), "NamaStok" to selectedObject.stockName, "QuantityStok" to itemStokQuantityTextView.text.toString())
                produkHashMap[i.toString()] = produkHashMapItem
            }

            val databaseHandler : DatabaseHandler = DatabaseHandler(this)

            if(!namaProduk.isEmpty() && layoutCount > 0 && !harga.isEmpty()){
                val produkId = intent.getIntExtra("produkId",0)
                val hargaProdukDubleValue = harga.toDouble()
                val status = databaseHandler.updateProduk(ProdukData(produkId, namaProduk,
                    "", galonLiter, produkHashMap, hargaProdukDubleValue)
                )
                if(status > -1){
                    Toast.makeText(applicationContext, "Produk berhasil diupdate!",Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                Toast.makeText(applicationContext, "Nama produk, item, dan harga tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }

        }
    }
}
