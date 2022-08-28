package com.rosariobagaskara.instock

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AddOrderActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        val addOrderProduk = findViewById<Button>(R.id.tambahProduk)
        val layoutListAddProduk = findViewById<LinearLayout>(R.id.layoutListProdukOrder)

        addOrderProduk.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val produkList : ArrayList<ProdukData> = databaseHandler.viewProduk()
            val listAddProduk = layoutInflater.inflate(R.layout.list_order_add_produk,null, false)
            val spinnerJenisProduk = listAddProduk.findViewById<Spinner>(R.id.pilihProdukSpinner)
            val deleteImageView = listAddProduk.findViewById<ImageView>(R.id.hapusAddOrderProduk)
            val arrowLeft = listAddProduk.findViewById<ImageView>(R.id.arrowLeftImage)
            val arrowRight = listAddProduk.findViewById<ImageView>(R.id.arrowRightImage)
            val quantityOrder = listAddProduk.findViewById<EditText>(R.id.quantityOrder)

            var minteger = 1
            quantityOrder.setText("" + minteger)
            spinnerJenisProduk.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, produkList)
            deleteImageView.setOnClickListener{
                layoutListAddProduk.removeView(listAddProduk)
            }

            arrowLeft.setOnClickListener {
                minteger--
                if(minteger<1){
                    minteger = 1
                }
                quantityOrder.setText("" + minteger)
            }
            arrowRight.setOnClickListener {
                minteger++
                quantityOrder.setText("" + minteger)
            }
            layoutListAddProduk.addView(listAddProduk)
        }

        addRecord()

    }

    private fun addRecord(){
        val submitOrder = findViewById<Button>(R.id.submitOrder)
        submitOrder.setOnClickListener {
            val databaseHandler : DatabaseHandler = DatabaseHandler(this)
            val namaPelanggan = findViewById<EditText>(R.id.addOrderNamaPelangganValue)
            val layoutListAddProduk = findViewById<LinearLayout>(R.id.layoutListProdukOrder)
            val layoutCount = layoutListAddProduk.childCount
            var orderHashMapProduk : HashMap<String,String> = HashMap<String,String>()
            val orderHashMap : HashMap<String,HashMap<String,String>> = HashMap<String,HashMap<String,String>>()
            var stokProdukHashMapDb : HashMap<String, String> = HashMap<String, String>()
            var statusStokHabis: Int = 0
            var itemStokHabis: String = ""
            var totalGalon: Int = 0
            for(i in 0 until layoutCount){
                val child = layoutListAddProduk.getChildAt(i)
                val produkSpinner = child.findViewById<Spinner>(R.id.pilihProdukSpinner)
                val quantityProduk = child.findViewById<EditText>(R.id.quantityOrder)

                val selectedObject = produkSpinner.selectedItem as ProdukData
                val hargaProduk = databaseHandler.getHargaProdukByProdukId(selectedObject.produkId)
                val totalHarga = hargaProduk * Integer.parseInt(quantityProduk.text.toString())
                totalGalon = Integer.parseInt(selectedObject.galonLiterValue.toString()) * Integer.parseInt(quantityProduk.text.toString())
                orderHashMapProduk = hashMapOf(
                    "ID" to selectedObject.produkId.toString(),
                    "NamaProduk" to selectedObject.namaProduk,
                    "JenisProduk" to selectedObject.jenisProduk,
                    "QuantityProduk" to quantityProduk.text.toString(),
                    "HargaProduk" to totalHarga.toString(),
                    "Liter" to totalGalon.toString()
                )
                orderHashMap[i.toString()] = orderHashMapProduk

                val itemProduk = databaseHandler.viewItemProdukByProdukId(selectedObject.produkId)
                val JsonStockProdukHashMap = JSONObject(itemProduk as Map<String, Map<String, String>>)
                val stockProdukHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonStockProdukHashMap.toString())
                for (i in 0 until stockProdukHashMap.size){
                    val index = stockProdukHashMap[i.toString()]
                    if (index != null) {
                        stokProdukHashMapDb = databaseHandler.getStockById(Integer.parseInt(index.get("ID")))
                        if(Integer.parseInt(stokProdukHashMapDb["QuantityStok"]) - (Integer.parseInt(index["QuantityStok"]) * Integer.parseInt(quantityProduk.text.toString())) < 0){
                            statusStokHabis = 1
                            itemStokHabis += "${stokProdukHashMapDb["NamaStok"].toString()}, "
                        }
                    }
                }

            }

            val namaPelangganValue : String = namaPelanggan.text.toString()
            val statusOrder = "Success"
            val currentDate = LocalDateTime.now()
            val currentDateString = currentDate.format(DateTimeFormatter.ISO_DATE)
            var stokUpdate :Int = 0

            if(!namaPelangganValue.isEmpty() && layoutCount > 0){
                if(statusStokHabis == 1){
                    Toast.makeText(applicationContext, "Item $itemStokHabis kehabisan stok!", Toast.LENGTH_LONG).show()
                }else{
                    val status = databaseHandler.addOrder(
                        OrderData(0, currentDateString,
                            namaPelangganValue, statusOrder, orderHashMap)
                    )
                    if(status > -1){
                        var statusUpdateStok = -1

                        for (i in 0 until orderHashMap.size){
                            Log.e("tes", orderHashMap.toString())

                            val index = orderHashMap[i.toString()]

                            if (index != null) {
                                val itemProduk = databaseHandler.viewItemProdukByProdukId(Integer.parseInt(index.get("ID")))
                                val JsonStockProdukHashMap = JSONObject(itemProduk as Map<String, Map<String, String>>)
                                val stockProdukHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonStockProdukHashMap.toString())
                                for (i in 0 until stockProdukHashMap.size) {
                                    val indexStock = stockProdukHashMap[i.toString()]
                                    if(indexStock != null){
                                        stokProdukHashMapDb = databaseHandler.getStockById(Integer.parseInt(indexStock.get("ID")))
                                        stokUpdate = Integer.parseInt(stokProdukHashMapDb["QuantityStok"]) - (Integer.parseInt(indexStock["QuantityStok"]) * Integer.parseInt(index["QuantityProduk"]))
                                        statusUpdateStok = databaseHandler.updateItemStokQuantityById(Integer.parseInt(stokProdukHashMapDb["ID"]), stokUpdate)
                                        databaseHandler.addLogStok(LogStokData(0, currentDateString, currentDateString, stokProdukHashMapDb["NamaStok"].toString(), (Integer.parseInt(indexStock["QuantityStok"]) * Integer.parseInt(index["QuantityProduk"])), "Order"))
                                    }
                                }
                            }
                            if(statusUpdateStok > -1){
                                Toast.makeText(applicationContext, "Order berhasil diinput!",Toast.LENGTH_LONG).show()
                            }
                        }
                        finish()
                    }
                }
            }else{
                Toast.makeText(applicationContext, "Nama order, dan produk tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }
        }
    }
}
