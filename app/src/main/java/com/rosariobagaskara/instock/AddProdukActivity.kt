package com.rosariobagaskara.instock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*

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
            val listProdukItem: Array<String> = arrayOf("Apel","Semangka")
            val listAddItem = layoutInflater.inflate(R.layout.list_produk_add_item, null, false)

            val spinnerProdukItem : Spinner = listAddItem.findViewById(R.id.spinnerAddProdukItem)
            val editJumlahAddProdukItem : EditText = listAddItem.findViewById(R.id.editJumlahAddProdukItem)
            val hapusProdukItem : ImageView = listAddItem.findViewById(R.id.hapusProdukItem)

            spinnerProdukItem.adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,listProdukItem)

            hapusProdukItem.setOnClickListener {
                layoutListProduk.removeView(listAddItem)
            }

            layoutListProduk.addView(listAddItem)
        }
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
}