package com.rosariobagaskara.instock

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var produkDataAdapter : ProdukDataAdapter
private lateinit var newRecyclerView : RecyclerView
private lateinit var arrayList: ArrayList<ProdukData>
lateinit var produkId: Array<Int>
lateinit var itemHarga: Array<Double>
lateinit var produkName: Array<String>
lateinit var jenisProduk: Array<String>
lateinit var galonLiterValue: Array<Int>
lateinit var itemProduk: HashMap<String,String>
lateinit var addProduk: FloatingActionButton

/**
 * A simple [Fragment] subclass.
 * Use the [ProdukFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProdukFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_produk, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProdukFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProdukFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addProduk = view.findViewById(R.id.addProduk)
        addProduk.setOnClickListener {
            val intent = Intent(activity, AddProdukActivity::class.java)
            startActivity(intent)
        }

        produkData()

        val layoutManager = LinearLayoutManager(context)
        newRecyclerView = view.findViewById(R.id.recyclerViewProduk)
        newRecyclerView.layoutManager = layoutManager
        newRecyclerView.setHasFixedSize(true)
        produkDataAdapter = ProdukDataAdapter(arrayList)
        newRecyclerView.adapter = produkDataAdapter
    }

    fun produkData(){
        produkId = arrayOf(1)
        produkName = arrayOf("Galon")
        itemProduk = HashMap<String, String>()
        itemProduk["ID"] = "1"
        itemProduk["NamaStok"] = "Tissue"
        itemProduk["QuantityStok"] = "1"
        itemHarga = arrayOf(5000.00)
        jenisProduk = arrayOf("Laundry")
        galonLiterValue = arrayOf(0)
        arrayList = arrayListOf<ProdukData>()

        for(i in produkId.indices){
//            val produkData = ProdukData(produkId[i], produkName[i], jenisProduk[i],galonLiterValue[i], itemProduk, itemHarga[i])
//            arrayList.add(produkData)
        }
    }
}