package com.rosariobagaskara.instock

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class UpdateStockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_stock)
        updateRecord()
    }

    private fun updateRecord(){
        val buttonUpdate = findViewById<Button>(R.id.updateStocks)
        val buttonCancel = findViewById<Button>(R.id.cancelUpdateStocks)

        val addItemNameValue = findViewById<EditText>(R.id.addItemNameValue)
        val editTextNumberDecimal = findViewById<EditText>(R.id.editTextNumberDecimal)
        val namaItem : String? = intent.getStringExtra("namaItem")
        val quantityItem: Int? = intent.getIntExtra("quantityItem", 0)
        addItemNameValue.setText(namaItem)
        editTextNumberDecimal.setText(quantityItem.toString())

        buttonCancel.setOnClickListener {
            finish()
        }

        buttonUpdate.setOnClickListener {
            val stockName = addItemNameValue.text.toString()
            val stockQuantity = editTextNumberDecimal.text.toString()
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)

            if(!stockName.isEmpty() && !stockQuantity.isEmpty()){
                val stockId: Int = intent.getIntExtra("idItem",0)
                val intStockQuantity = Integer.parseInt(stockQuantity)
                val status = databaseHandler.updateItemStok(StockData(stockId, stockName, intStockQuantity))
                if(status > -1){
                    Toast.makeText(applicationContext, "Item berhasil diupdate!",Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                Toast.makeText(applicationContext, "Nama item dan jumlah tidak boleh kosong", Toast.LENGTH_LONG).show()
            }
        }
    }
}
