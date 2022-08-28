package com.rosariobagaskara.instock

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject

class DatabaseHandler(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object{
        private val DATABASE_VERSION = 9
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

        private val TABLE_ORDER = "OrderTable"
        private val orderId = "order_id"
        private val orderDate = "order_date"
        private val namaPemesan = "order_nama_pemesan"
        private val orderStatus = "order_status"
        private val orderProduk = "order_produk"

        private val TABLE_LOG_STOK = "LogStokTable"
        private val logStokId = "log_stok_id"
        private val logStokDate = "log_stok_date"
        private val logStokOrderDate = "log_stok_order_date"
        private val logStokName = "log_stok_name"
        private val logStokQuantity = "log_stok_quantity"
        private val logStokStatus = "log_stok_status"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createStokTable = ("CREATE TABLE $TABLE_STOK ( $stokId INTEGER PRIMARY KEY, $stokName TEXT, $stokQuantity INTEGER)")
        p0?.execSQL(createStokTable)

        val createProdukTable = ("CREATE TABLE $TABLE_PRODUK ( $produkId INTEGER PRIMARY KEY, $produkName TEXT, $jenisProduk TEXT, $galonLiter INTEGER, $itemProduk VARCHAR(255), $hargaProduk DOUBLE )")
        p0?.execSQL(createProdukTable)

        val createOrderTable = ("CREATE TABLE $TABLE_ORDER ( $orderId INTEGER PRIMARY KEY, $orderDate DATETIME, $namaPemesan TEXT, $orderStatus TEXT, $orderProduk VARCHAR(255))")
        p0?.execSQL(createOrderTable)

        val createLogStokTable = ("CREATE TABLE $TABLE_LOG_STOK ( $logStokId INTEGER PRIMARY KEY, $logStokDate DATETIME, $logStokOrderDate DATETIME, $logStokName TEXT, $logStokQuantity INTEGER, $logStokStatus TEXT)")
        p0?.execSQL(createLogStokTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_STOK")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUK")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_ORDER")
        p0?.execSQL("DROP TABLE IF EXISTS $TABLE_LOG_STOK")
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

    fun updateItemStokQuantityById(stockIdValue: Int, stockQuantityValue: Int): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(stokQuantity, stockQuantityValue)

        // Updating Row
        val success = db.update(TABLE_STOK, contentValue, stokId + "=" + stockIdValue, null)
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

    fun addOrder(orderData: OrderData): Long{
        val db = this.writableDatabase
        val contentValue = ContentValues()

        contentValue.put(orderDate, orderData.dateOrder)
        contentValue.put(namaPemesan, orderData.namaPemesan)
        contentValue.put(orderStatus, orderData.statusOrder)
        contentValue.put(orderProduk, JSONObject(orderData.orderProduk as Map<String, Map<String, String>>).toString())

        val success = db.insert(TABLE_ORDER, null, contentValue)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun viewOrder(): ArrayList<OrderData>{
        val orderList : ArrayList<OrderData> = ArrayList<OrderData>()

        val selectQuery = "SELECT * FROM $TABLE_ORDER"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var orderIdList : Int
        var dateOrderList : String
        var namaPemesanList : String
        var statusOrderList : String
        var orderProdukList : String

        if(cursor.moveToFirst()){
            do{

                orderIdList = cursor.getInt(cursor.getColumnIndex(orderId))
                dateOrderList = cursor.getString(cursor.getColumnIndex(orderDate))
                namaPemesanList = cursor.getString(cursor.getColumnIndex(namaPemesan))
                statusOrderList = cursor.getString(cursor.getColumnIndex(orderStatus))
                orderProdukList = cursor.getString(cursor.getColumnIndex(orderProduk))

                val hashMapOrderList = Json.decodeFromString<HashMap<String, HashMap<String, String>>>(orderProdukList)
                val od = OrderData(orderId = orderIdList, dateOrder = dateOrderList, namaPemesan = namaPemesanList, statusOrder = statusOrderList, orderProduk = hashMapOrderList)
                orderList.add(od)
            }while (cursor.moveToNext())
        }
        return orderList
    }

    @SuppressLint("Range")
    fun getOrderByStatusAndDate(status:String, date1: String, date2: String): ArrayList<OrderData>{
        val orderList : ArrayList<OrderData> = ArrayList<OrderData>()

        val selectQuery = "SELECT * FROM $TABLE_ORDER WHERE $orderStatus = '$status' AND $orderDate BETWEEN '$date1' AND '$date2'"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var orderIdList : Int
        var dateOrderList : String
        var namaPemesanList : String
        var statusOrderList : String
        var orderProdukList : String

        if(cursor.moveToFirst()){
            do{

                orderIdList = cursor.getInt(cursor.getColumnIndex(orderId))
                dateOrderList = cursor.getString(cursor.getColumnIndex(orderDate))
                namaPemesanList = cursor.getString(cursor.getColumnIndex(namaPemesan))
                statusOrderList = cursor.getString(cursor.getColumnIndex(orderStatus))
                orderProdukList = cursor.getString(cursor.getColumnIndex(orderProduk))

                val hashMapOrderList = Json.decodeFromString<HashMap<String, HashMap<String, String>>>(orderProdukList)
                val od = OrderData(orderId = orderIdList, dateOrder = dateOrderList, namaPemesan = namaPemesanList, statusOrder = statusOrderList, orderProduk = hashMapOrderList)
                orderList.add(od)
            }while (cursor.moveToNext())
        }
        return orderList
    }

    @SuppressLint("Range")
    fun getOrderById(orderIdValue: Int): OrderData{
        var orderList : OrderData = OrderData(0, "", "", "", hashMapOf())

        val selectQuery = "SELECT * FROM $TABLE_ORDER WHERE $orderId = $orderIdValue"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return orderList
        }

        var orderIdList : Int
        var dateOrderList : String
        var namaPemesanList : String
        var statusOrderList : String
        var orderProdukList : String

        if(cursor.moveToFirst()){
            do{
                orderIdList = cursor.getInt(cursor.getColumnIndex(orderId))
                dateOrderList = cursor.getString(cursor.getColumnIndex(orderDate))
                namaPemesanList = cursor.getString(cursor.getColumnIndex(namaPemesan))
                statusOrderList = cursor.getString(cursor.getColumnIndex(orderStatus))
                orderProdukList = cursor.getString(cursor.getColumnIndex(orderProduk))

                val hashMapOrderList = Json.decodeFromString<HashMap<String, HashMap<String, String>>>(orderProdukList)
                orderList = OrderData(orderId = orderIdList, dateOrder = dateOrderList, namaPemesan = namaPemesanList, statusOrder = statusOrderList, orderProduk = hashMapOrderList)
            }while (cursor.moveToNext())
        }
        return orderList
    }

    fun cancelOrder(orderData: OrderData): Int {
        val db = this.writableDatabase
        val contentValue = ContentValues()
        contentValue.put(orderStatus, orderData.statusOrder)

        // Updating Row
        val success = db.update(TABLE_ORDER, contentValue, orderId + "=" + orderData.orderId, null)
        //2nd argument is String containing nullColumnHack

        // Closing database connection
        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getStockById(stokIdValue: Int): HashMap<String,String> {
        var stokHashMap : HashMap<String,String> = HashMap<String,String>()

        val selectQuery = "SELECT * FROM $TABLE_STOK WHERE $stokId = $stokIdValue"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return HashMap<String,String>()
        }
        var id: Int
        var name: String
        var quantity: Int
        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(stokId))
            name = cursor.getString(cursor.getColumnIndex(stokName))
            quantity = cursor.getInt(cursor.getColumnIndex(stokQuantity))
            stokHashMap = hashMapOf("ID" to id.toString(), "NamaStok" to name, "QuantityStok" to quantity.toString())
        }
        return stokHashMap
    }

    @SuppressLint("Range")
    fun viewItemProdukByProdukId(produkIdValue : Int): HashMap<String, HashMap<String, String>>{

        val selectQuery = "SELECT $itemProduk FROM $TABLE_PRODUK WHERE $produkId = $produkIdValue"
        val db = this.readableDatabase
        var hashMapProdukList : HashMap<String, HashMap<String, String>> = HashMap<String, HashMap<String, String>>()
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return hashMapOf()
        }

        var itemProdukList : String

        if(cursor.moveToNext()){
            itemProdukList = cursor.getString(cursor.getColumnIndex(itemProduk))
            hashMapProdukList = Json.decodeFromString<HashMap<String, HashMap<String, String>>>(itemProdukList)
        }
        return hashMapProdukList
    }

    @SuppressLint("Range")
    fun getHargaProdukByProdukId(produkIdValue : Int): Double{

        val selectQuery = "SELECT $hargaProduk FROM $TABLE_PRODUK WHERE $produkId = $produkIdValue"
        val db = this.readableDatabase
        var hargaProdukValue : Double = 0.0
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return 0.0
        }


        if(cursor.moveToNext()){
            hargaProdukValue = cursor.getDouble(cursor.getColumnIndex(hargaProduk))
        }
        return hargaProdukValue
    }

    fun addLogStok(logStokData: LogStokData): Long{
        val db = this.writableDatabase

        val contentValue = ContentValues()

        contentValue.put(logStokDate, logStokData.logStokDate)
        contentValue.put(logStokOrderDate, logStokData.logStokOrderDate)
        contentValue.put(logStokName, logStokData.logStokName)
        contentValue.put(logStokQuantity, logStokData.logStokQuantity)
        contentValue.put(logStokStatus, logStokData.logStokStatus)

        val success = db.insert(TABLE_LOG_STOK, null, contentValue)

        db.close()
        return success
    }

    @SuppressLint("Range")
    fun getLogStockNameAndSumByOrderDate(date1: String, date2: String): ArrayList<LogStokData> {
        var logStokList : ArrayList<LogStokData> = ArrayList<LogStokData>()
        val selectQuery = "SELECT $logStokName, SUM($logStokQuantity) AS $logStokQuantity FROM $TABLE_LOG_STOK WHERE $logStokOrderDate BETWEEN '$date1' AND '$date2' GROUP BY $logStokName"

        val db = this.readableDatabase
        var cursor: Cursor? = null

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch (e : SQLiteException){
            db.execSQL(selectQuery)
            return ArrayList<LogStokData>()
        }
        var name: String
        var quantity: Int
        if(cursor.moveToFirst()){
            do{
                name = cursor.getString(cursor.getColumnIndex(logStokName))
                quantity = cursor.getInt(cursor.getColumnIndex(logStokQuantity))
                val ld = LogStokData(0,"","", name, quantity, "")
                logStokList.add(ld)
            }while(cursor.moveToNext())

        }
        return logStokList
    }
}