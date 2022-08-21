package com.rosariobagaskara.instock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*
import kotlin.collections.ArrayList

class AddStockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)
        val stockId: Int = intent.getIntExtra("idItem", 0)
        val namaItem : String? = intent.getStringExtra("namaItem")
        val quantityItem: Int = intent.getIntExtra("quantityItem", 0)
        if(stockId == null){
//            addRecord()
        }else if(stockId != null){
            updateRecord()
        }

    }

    private fun addRecord(){
        val buttonSubmit = findViewById<Button>(R.id.submitOrder)
        buttonSubmit.setOnClickListener {
            val addItemNameValue = findViewById<EditText>(R.id.addItemNameValue)
            val editTextNumberDecimal = findViewById<EditText>(R.id.editTextNumberDecimal)
            val stockName = addItemNameValue.text.toString()
            val stockQuantity = editTextNumberDecimal.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if(!stockName.isEmpty() && !stockQuantity.isEmpty()){

                val intStockQuantity = Integer.parseInt(stockQuantity)

                val status = databaseHandler.addItemStok(StockData(0, stockName, intStockQuantity))
                if(status > -1){
                    Toast.makeText(applicationContext, "Item berhasil diinput!",Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                Toast.makeText(applicationContext, "Nama item dan jumlah tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateRecord(){
        val buttonSubmit = findViewById<Button>(R.id.submitOrder)

        val addItemNameValue = findViewById<EditText>(R.id.addItemNameValue)
        val editTextNumberDecimal = findViewById<EditText>(R.id.editTextNumberDecimal)
        val namaItem : String? = intent.getStringExtra("namaItem")
        val quantityItem: Int? = intent.getIntExtra("quantityItem", 0)
        addItemNameValue.setText(namaItem)
        editTextNumberDecimal.setText(quantityItem.toString())

        buttonSubmit.setOnClickListener {
            val stockName = addItemNameValue.text.toString()
            val stockQuantity = editTextNumberDecimal.text.toString()
            val stockId: String? = intent.getStringExtra("idItem")
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if(!stockName.isEmpty() && !stockQuantity.isEmpty()){

                val intStockQuantity = Integer.parseInt(stockQuantity)
                var stockIdInt = 0
                stockIdInt = stockId?.toInt() ?: 0
                val status = databaseHandler.addItemStok(StockData(stockIdInt, stockName, intStockQuantity))
                if(status > -1){
                    Toast.makeText(applicationContext, "Item berhasil diinput!",Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                Toast.makeText(applicationContext, "Nama item dan jumlah tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
        }
    }
}
