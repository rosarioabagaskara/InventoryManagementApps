package com.rosariobagaskara.instock

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf
import com.google.gson.Gson
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 3
        private val DATABASE_NAME = "anindatu_database"

        private val TABLE_STOK = "StokTable"
        private val stokId = "stok_id"
        private val stokName = "stok_name"
        private val stokQuantity = "stok_quantity"

        private val TABLE_PRODUK = "ProdukTable"
        private val produkId = "produk_id"
        private val produkName = "produk_name"
        private val jenisProduk = "produk_jenis"
        private val galonLiter = "produk_liter_galon"
        private val itemProduk = "produk_item"
        private val hargaProduk = "produk_harga"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createStokTable = ("CREATE TABLE $TABLE_STOK ( $stokId INTEGER PRIMARY KEY, $stokName TEXT, $stokQuantity INTEGER)")
        p0?.execSQL(createStokTable)

        val createProdukTable = ("CREATE TABLE $TABLE_PRODUK ( $produkId INTEGER PRIMARY KEY, $produkName TEXT, $jenisProduk TEXT, $galonLiter INTEGER, $itemProduk VARCHAR(255), $hargaProduk DOUBLE )")
        p0?.execSQL(createProdukTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_STOK")
        onCreate(p0)
    }

    fun addItemStok(stockData: StockData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()

        contentValue.put(stokName, stockData.stockName)
        contentValue.put(stokQuantity, stockData.stockQuantity)

        val success = db.insert(TABLE_STOK, null, contentValue)

        db.close()
        return success
    }

    fun updateItemStok(stockData: StockData): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(stokName, stockData.stockName)
        contentValue.put(stokQuantity, stockData.stockQuantity)

        // Updating Row
        val success = db.update(TABLE_STOK, contentValue, stokId + "=" + stockData.stockId, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    fun deleteItemStok(stockData: StockData): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(stokId, stockData.stockId)

        // Updating Row
        val success = db.delete(TABLE_STOK, stokId + "=" + stockData.stockId, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewItemStock(): ArrayList<StockData>{
        val itemStockList : ArrayList<StockData> = ArrayList<StockData>()

        val selectQuery = "SELECT * FROM $TABLE_STOK"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name: String
        var quantity: Int

        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(cursor.getColumnIndex(stokId))
                name = cursor.getString(cursor.getColumnIndex(stokName))
                quantity = cursor.getInt(cursor.getColumnIndex(stokQuantity))

                val sd = StockData(stockId = id, stockName = name, stockQuantity = quantity)
                itemStockList.add(sd)
            }while (cursor.moveToNext())
        }
        return itemStockList
    }

    fun addProduk(produkData: ProdukData): Long{
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(produkName, produkData.namaProduk)
        contentValue.put(jenisProduk, produkData.jenisProduk)
        contentValue.put(galonLiter, produkData.galonLiterValue)
        contentValue.put(itemProduk, JSONObject(produkData.itemProduk as Map<String, Map<String, String>>).toString())
        contentValue.put(hargaProduk, produkData.harga)

        val success = db.insert(TABLE_PRODUK, null, contentValue)

        db.close()
        return success
    }

    fun updateProduk(produkData: ProdukData): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(produkName, produkData.namaProduk)
        contentValue.put(galonLiter, produkData.galonLiterValue)
        contentValue.put(itemProduk, JSONObject(produkData.itemProduk as Map<String, Map<String, String>>).toString())
        contentValue.put(hargaProduk, produkData.harga)

        // Updating Row
        val success = db.update(TABLE_PRODUK, contentValue, produkId + "=" + produkData.produkId, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewProduk(): ArrayList<ProdukData>{
        val produkList : ArrayList<ProdukData> = ArrayList<ProdukData>()

        val selectQuery = "SELECT * FROM $TABLE_PRODUK"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var produkIdList : Int
        var produkNameList : String
        var jenisProdukList : String
        var galonLiterList : Int
        var itemProdukList : String
        var hargaProdukList : Double

        if(cursor.moveToFirst()){
            do{
                produkIdList = cursor.getInt(cursor.getColumnIndex(produkId))
                produkNameList = cursor.getString(cursor.getColumnIndex(produkName))
                jenisProdukList = cursor.getString(cursor.getColumnIndex(jenisProduk))
                galonLiterList = cursor.getInt(cursor.getColumnIndex(galonLiter))
                itemProdukList = cursor.getString(cursor.getColumnIndex(itemProduk))
                hargaProdukList = cursor.getDouble(cursor.getColumnIndex(hargaProduk))
                val hashMapProdukList = Json.decodeFromString<HashMap<String, HashMap<String, String>>>(itemProdukList)
                val pd = ProdukData(produkId = produkIdList, namaProduk = produkNameList, jenisProduk = jenisProdukList, galonLiterValue = galonLiterList, itemProduk = hashMapProdukList, harga = hargaProdukList)
                produkList.add(pd)
            }while (cursor.moveToNext())
        }
        return produkList
    }

    fun deleteProduk(produkData: ProdukData): Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(produkId,produkData.produkId)

        val success = db.delete(TABLE_PRODUK, produkId + "=" + produkData.produkId, null)

        db.close()

        return success

    }
}