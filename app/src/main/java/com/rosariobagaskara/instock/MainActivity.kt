package com.rosariobagaskara.instock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var newRecyclerView : RecyclerView
    private lateinit var arrayList: ArrayList<OrderData>
    lateinit var orderNumber: Array<String>
    lateinit var orderDate: Array<String>
    lateinit var namaPemesan: Array<String>
    lateinit var item: Array<String>
    lateinit var itemNumber: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        orderNumber = arrayOf("Order #1","Order #2","Order #3","Order #4","Order #5","Order #6","Order #7","Order #8", "Order #9","Order #10","Order #11","Order #12","Order #13","Order #14","Order #15","Order #16")
        orderDate = arrayOf("02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022")
        namaPemesan = arrayOf("Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha")
        item = arrayOf("Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon")
        itemNumber = arrayOf("x1", "x1","x2","x4","x1", "x1","x2","x4","x1", "x1","x2","x4","x1", "x1","x2","x4")

        newRecyclerView = findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        arrayList = arrayListOf<OrderData>()
        getUserData()
    }

    private fun getUserData() {
        for(i in orderNumber.indices){
            val orderData = OrderData(orderNumber[i], orderDate[i], namaPemesan[i], item[i], itemNumber[i])
            arrayList.add(orderData)
        }

        newRecyclerView.adapter = OrderDataAdapter(arrayList)
    }
}