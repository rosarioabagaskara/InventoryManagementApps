package com.rosariobagaskara.instock

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "anindatu_database"

        private val TABLE_STOK = "StokTable"
        private val stokId = "stok_id"
        private val stockName = "stok_name"
        private val stockQuantity = "stok_quantity"

    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createStokTable = ("CREATE TABLE $TABLE_STOK ($stokId INTEGER_PRIMARY_KEY, $stockName TEXT, $stockQuantity INTEGER)")
        p0?.execSQL(createStokTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_STOK")
        onCreate(p0)
    }

    fun addItemStok(stockData: StockData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()

        contentValue.put(stockName, stockData.stockName)
        contentValue.put(stockQuantity, stockData.stockQuantity)

        val success = db.insert(TABLE_STOK, null, contentValue)

        db.close()
        return success

    }
}