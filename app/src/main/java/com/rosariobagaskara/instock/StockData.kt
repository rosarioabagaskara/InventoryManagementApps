package com.rosariobagaskara.instock

data class StockData(var stockId: Int, var stockName: String, var stockQuantity: Int){
    override fun toString(): String {
        return stockName
    }
}
