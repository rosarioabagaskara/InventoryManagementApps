package com.rosariobagaskara.instock

data class ProdukData(val produkId: Int, val namaProduk: String, val jenisProduk: String, val galonLiterValue: Int, val itemProduk: HashMap<String, HashMap<String, String>>, val harga: Double)