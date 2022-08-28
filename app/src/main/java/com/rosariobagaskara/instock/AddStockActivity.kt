package com.rosariobagaskara.instock

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddStockActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_stock)
        addRecord()
    }

    private fun addRecord(){
        val buttonSubmit = findViewById<Button>(R.id.submitStock)
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
}
