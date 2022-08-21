package com.rosariobagaskara.instock

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rosariobagaskara.instock.databinding.ActivityMainBinding

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
        val intent = Intent(this, AddStockActivity::class.java)
        Log.e("tes", stockData.toString())
        intent.putExtra("idItem",stockData.stockId)
        intent.putExtra("namaItem",stockData.stockName)
        intent.putExtra("quantityItem",stockData.stockQuantity)
        startActivity(intent)
    }
}