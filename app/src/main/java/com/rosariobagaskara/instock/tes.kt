package com.rosariobagaskara.instock

fun main(){
    val hash2 : HashMap<String, HashMap<String, Int>> = HashMap()
    for(i in 0 until 2){
        val hash1 = hashMapOf("key1" to 1+i, "key2" to 2+i, "key3" to 3+i, "key4" to 1+i)
        hash2.put(i.toString(), hash1)
    }




    print(hash2)
}