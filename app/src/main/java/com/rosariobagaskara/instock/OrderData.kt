package com.rosariobagaskara.instock

data class OrderData(var orderId: Int, var dateOrder: String, var namaPemesan: String, var statusOrder: String, val orderProduk: HashMap<String, HashMap<String, String>>)