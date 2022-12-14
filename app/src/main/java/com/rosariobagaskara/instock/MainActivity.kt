package com.rosariobagaskara.instock

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.rosariobagaskara.instock.databinding.ActivityMainBinding
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(HomeFragment())
        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.btnHome -> replaceFragment(HomeFragment())
                R.id.btnProduk -> replaceFragment(ProdukFragment())
                R.id.btnStock -> replaceFragment(StockFragment())
                R.id.btnLaporan -> replaceFragment(ReportFragment())
                else -> {
                }
            }
            true
        }
    }

    fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }

    fun updateListStock(stockData: StockData){
        val intent = Intent(this, UpdateStockActivity::class.java)
        intent.putExtra("idItem",stockData.stockId)
        intent.putExtra("namaItem",stockData.stockName)
        intent.putExtra("quantityItem",stockData.stockQuantity)
        startActivity(intent)
    }

    fun deleteListStock(stockData: StockData){
        val builder = AlertDialog.Builder(this)
        //set title for alert dialog
        builder.setTitle("Hapus Stok")
        //set message for alert dialog
        builder.setMessage("Yakin mau menghapus ${stockData.stockName}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        //performing positive action
        builder.setPositiveButton("Yes") { dialogInterface, which ->

            //creating the instance of DatabaseHandler class
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            //calling the deleteEmployee method of DatabaseHandler class to delete record
            val status = databaseHandler.deleteItemStok(StockData(stockData.stockId, "", 0))
            if (status > -1) {
                Toast.makeText(
                    applicationContext,
                    "Stock berhasil dihapus.",
                    Toast.LENGTH_LONG
                ).show()
            }

            dialogInterface.dismiss() // Dialog will be dismissed
        }
        //performing negative action
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss() // Dialog will be dismissed
        }
        // Create the AlertDialog
        val alertDialog: AlertDialog = builder.create()
        // Set other dialog properties
        alertDialog.setCancelable(false) // Will not allow user to cancel after clicking on remaining screen area.
        alertDialog.show()  // show the dialog to UI
    }

    fun updateProduk(produkData: ProdukData){
        val intent: Intent = Intent(this, UpdateProdukActivity::class.java)
        intent.putExtra("produkId", produkData.produkId)
        intent.putExtra("produkName", produkData.namaProduk)
        intent.putExtra("jenisProduk", produkData.jenisProduk)
        intent.putExtra("galonLiterValue", produkData.galonLiterValue)
        intent.putExtra("itemProduk", JSONObject(produkData.itemProduk as Map<String, Map<String, String>>).toString())
        intent.putExtra("harga", produkData.harga)
        startActivity(intent)
    }

    fun deleteProduk(produkData: ProdukData){
        var builder = AlertDialog.Builder(this)

        builder.setTitle("Hapus Produk")
        builder.setMessage("Yakin ingin menghapus ${produkData.namaProduk}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            val databaseHandler : DatabaseHandler = DatabaseHandler(this)

            val status = databaseHandler.deleteProduk(ProdukData(produkData.produkId, "","",0, hashMapOf(),0.0))

            if(status > -1){
                Toast.makeText(this, "Produk berhasil dihapus!", Toast.LENGTH_SHORT).show()
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    fun cancelOrder(orderData: OrderData){
        var builder = AlertDialog.Builder(this)
        val currentDate = LocalDateTime.now()
        val currentDateString = currentDate.format(DateTimeFormatter.ISO_DATE)
        builder.setTitle("Cancel Order")
        builder.setMessage("Yakin ingin cancel order #${orderData.orderId}?")
        builder.setIcon(android.R.drawable.ic_dialog_alert)

        builder.setPositiveButton("Yes") { dialogInterface, which ->

            val databaseHandler : DatabaseHandler = DatabaseHandler(this)

            val status = databaseHandler.cancelOrder(OrderData(orderData.orderId, "", "", "Canceled", hashMapOf()))

            if(status > -1){
                var statusUpdateStok = -1
                val orderById = databaseHandler.getOrderById(orderData.orderId)
                val JsonProdukOrderHashMap = JSONObject(orderById.orderProduk as Map<String, Map<String, String>>)
                val orderHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukOrderHashMap.toString())
                var stokProdukHashMapDb : HashMap<String, String> = HashMap<String, String>()
                var stokUpdate :Int = 0
                for (i in 0 until orderHashMap.size){
                    val index = orderHashMap[i.toString()]

                    if (index != null) {
                        val itemProduk = databaseHandler.viewItemProdukByProdukId(Integer.parseInt(index.get("ID")))
                        val JsonStockProdukHashMap = JSONObject(itemProduk as Map<String, Map<String, String>>)
                        val stockProdukHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonStockProdukHashMap.toString())
                        for (i in 0 until stockProdukHashMap.size) {
                            val indexStock = stockProdukHashMap[i.toString()]
                            if(indexStock != null){
                                stokProdukHashMapDb = databaseHandler.getStockById(Integer.parseInt(indexStock.get("ID")))
                                stokUpdate = Integer.parseInt(stokProdukHashMapDb["QuantityStok"]) + (Integer.parseInt(indexStock["QuantityStok"]) * Integer.parseInt(index["QuantityProduk"]))
                                statusUpdateStok = databaseHandler.updateItemStokQuantityById(Integer.parseInt(stokProdukHashMapDb["ID"]), stokUpdate)
                                databaseHandler.addLogStok(LogStokData(0, currentDateString, orderById.dateOrder, stokProdukHashMapDb["NamaStok"].toString(), -(Integer.parseInt(indexStock["QuantityStok"]) * Integer.parseInt(index["QuantityProduk"])), "Cancel"))
                            }
                        }
                    }
                    if(statusUpdateStok > -1){
                        Toast.makeText(this, "Order berhasil dicancel!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialogInterface.dismiss()
        }

        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }

        val alertDialog : AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

}