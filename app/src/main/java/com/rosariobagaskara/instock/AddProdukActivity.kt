package com.rosariobagaskara.instock

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddProdukActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_produk)
        val listJenisProduk: Array<String> = arrayOf(" ","Galon","Laundry")
        val addItemBtn = findViewById<Button>(R.id.tambahItemProduk)
        val layoutListProduk = findViewById<LinearLayout>(R.id.layoutListProduk)
        val spinnerJenisProduk = findViewById<Spinner>(R.id.spinnerJenisProduk)

        spinnerJenisProduk.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listJenisProduk)
        spinnerJenisProduk.onItemSelectedListener = this

        addItemBtn.setOnClickListener {
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val itemStokList : ArrayList<StockData> = databaseHandler.viewItemStock()
            val listAddItem = layoutInflater.inflate(R.layout.list_produk_add_item, null, false)

            val spinnerProdukItem : Spinner = listAddItem.findViewById(R.id.spinnerAddProdukItem)
            val editJumlahAddProdukItem : EditText = listAddItem.findViewById(R.id.editJumlahAddProdukItem)
            val hapusProdukItem : ImageView = listAddItem.findViewById(R.id.hapusProdukItem)

            spinnerProdukItem.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,itemStokList)

            hapusProdukItem.setOnClickListener {
                layoutListProduk.removeView(listAddItem)
            }

            layoutListProduk.addView(listAddItem)
        }

        addRecord()
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        var items = p0?.getItemAtPosition(p2) as String
        val editTextLiterPerGalon = findViewById<EditText>(R.id.editTextLiterPerGalon)
        if(items == "Galon"){
            editTextLiterPerGalon.visibility = View.VISIBLE
        }else if(items != "Galon"){
            editTextLiterPerGalon.visibility = View.GONE
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }

    private fun addRecord(){
        val buttonSubmit = findViewById<Button>(R.id.submitProduk)

        buttonSubmit.setOnClickListener {
            val namaProdukTextView = findViewById<EditText>(R.id.addProdukNameValue)
            val spinnerJenisProduk = findViewById<Spinner>(R.id.spinnerJenisProduk)
            val galonLiterTextView = findViewById<EditText>(R.id.editTextLiterPerGalon)
            val mataUangEditText = findViewById<EditText>(R.id.mataUangValue)
            val layoutListProduk = findViewById<LinearLayout>(R.id.layoutListProduk)
            val layoutCount = layoutListProduk.childCount
            var produkHashMapItem : HashMap<String,String> = HashMap<String,String>()
            val produkHashMap : HashMap<String,HashMap<String,String>> = HashMap<String,HashMap<String,String>>()
            for(i in 0 until layoutCount){
                val child = layoutListProduk.getChildAt(i)
                val itemStokSpinner = child.findViewById<Spinner>(R.id.spinnerAddProdukItem)
                val itemStokQuantityTextView = child.findViewById<EditText>(R.id.editJumlahAddProdukItem)
                val selectedObject = itemStokSpinner.selectedItem as StockData
                produkHashMapItem = hashMapOf("ID" to selectedObject.stockId.toString(), "NamaStok" to selectedObject.stockName, "QuantityStok" to itemStokQuantityTextView.text.toString())
                produkHashMap[i.toString()] = produkHashMapItem
            }
            val namaProdukValue = namaProdukTextView.text.toString()
            val spinnerJenisProdukValue = spinnerJenisProduk.selectedItem.toString()
            var galonLiterValue = 0
            if (spinnerJenisProdukValue == "Galon"){
                galonLiterValue = Integer.parseInt(galonLiterTextView.text.toString())
            }
            var hargaProdukValue = mataUangEditText.text.toString()
            val databaseHandler : DatabaseHandler = DatabaseHandler(this)

            if(!namaProdukValue.isEmpty() && layoutCount > 0 && !hargaProdukValue.isEmpty()){
                val hargaProdukDubleValue = hargaProdukValue.toDouble()
                val status = databaseHandler.addProduk(ProdukData(0, namaProdukValue,
                    spinnerJenisProdukValue, galonLiterValue, produkHashMap, hargaProdukDubleValue)
                )
                if(status > -1){
                    Toast.makeText(applicationContext, "Produk berhasil diinput!",Toast.LENGTH_LONG).show()
                    finish()
                }
            }else{
                Toast.makeText(applicationContext, "Nama produk, item, dan harga tidak boleh kosong!", Toast.LENGTH_LONG).show()
            }
        }
    }
}

