package com.rosariobagaskara.instock

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "anindatu_database"

        private val TABLE_STOK = "StokTable"
        private val stokId = "stok_id"
        private val stokName = "stok_name"
        private val stokQuantity = "stok_quantity"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createStokTable = ("CREATE TABLE" + TABLE_STOK +"("+ stokId + "INTEGER_PRIMARY_KEY," + stokName +"TEXT," + stokQuantity + "INTEGER"+")")
        p0?.execSQL(createStokTable)
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
}