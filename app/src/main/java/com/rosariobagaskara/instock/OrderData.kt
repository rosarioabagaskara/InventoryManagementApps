package com.rosariobagaskara.instock

data class OrderData(var orderId: Int, var orderNumber: String, var dateOrder: String, var namaPemesan: String, var statusOrder: String, var item: String, var itemNumber: Int)