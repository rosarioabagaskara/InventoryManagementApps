package com.rosariobagaskara.instock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class AddOrderActivity : AppCompatActivity()
//    , AdapterView.OnItemSelectedListener
{
    val listJenisProduk = arrayOf("","Galon", "Laundry Kilat (1 sabun, 1 pewangi)", "Laundry Biasa (1 sabun, 1 pewangi)")
    val listItemProdukGalon = HashMap<String, Array<String>>()
    val listItemQuantityProdukGalon = HashMap<String, Array<Int>>()
    val listItemProduk = arrayOf(arrayOf(""),arrayOf("Tutup Botol", "Tissue"), arrayOf("Sabun", "Pewangi"), arrayOf("Sabun", "Pewangi"))
    val listItemValue = arrayOf(arrayOf(0),arrayOf(1, 1), arrayOf(1, 1), arrayOf(1, 1))
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_order)
        val addOrderProduk = findViewById<Button>(R.id.tambahProduk)
        val layoutListAddProduk = findViewById<LinearLayout>(R.id.layoutListAddOrderProduk)


        for(i in listJenisProduk.indices){
            listItemProdukGalon.put(listJenisProduk[i], listItemProduk[i])
            listItemQuantityProdukGalon.put(listJenisProduk[i], listItemValue[i])
        }


        addOrderProduk.setOnClickListener {
            val listAddProduk = layoutInflater.inflate(R.layout.list_order_add_produk,null, false)
            val spinnerJenisProduk = listAddProduk.findViewById<Spinner>(R.id.pilihProdukSpinner)
            val deleteImageView = listAddProduk.findViewById<ImageView>(R.id.hapusAddOrderProduk)
            val arrowLeft = listAddProduk.findViewById<ImageView>(R.id.arrowLeftImage)
            val arrowRight = listAddProduk.findViewById<ImageView>(R.id.arrowRightImage)
            val quantityOrder = listAddProduk.findViewById<EditText>(R.id.quantityOrder)
            var minteger = 1
            spinnerJenisProduk.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listJenisProduk)
//            spinnerJenisProduk.onItemSelectedListener = this

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
    }

//    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        val listAddProduk1 = layoutInflater.inflate(R.layout.list_order_add_produk,null, true)
//        var produk = p0?.getItemAtPosition(p2) as String
//        val orderProdukList = listAddProduk1.findViewById<TextView>(R.id.orderProdukListItemValueTextView)
//        val orderProdukListQuantity = listAddProduk1.findViewById<TextView>(R.id.orderProdukListItemQuantityValueTextView)
//        var orderProdukListValue = ""
//        var orderProdukListQuantityValue = ""
//        orderProdukList.visibility = View.VISIBLE
//        orderProdukListQuantity.visibility = View.VISIBLE
//        orderProdukList.setText("tes")
//        orderProdukListQuantity.setText("1")
//        Log.e("produk", produk)
//        if(listJenisProduk.contains(produk) && produk!= ""){
//            Log.e("produk", "berhasil")
//            var list = listItemProdukGalon.getValue(produk)
//            for (value in list){
//                Log.e("produk", value)
//                    orderProdukListValue += "$value\n"
//                    orderProdukListQuantityValue += "$value\n"
//            }
//        }
//    }
//
//    override fun onNothingSelected(p0: AdapterView<*>?) {
//    }
}
