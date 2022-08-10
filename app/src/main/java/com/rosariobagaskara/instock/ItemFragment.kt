package com.rosariobagaskara.instock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var itemDataAdapter : ItemDataAdapter
private lateinit var newRecyclerView : RecyclerView
private lateinit var arrayList: ArrayList<ItemData>
lateinit var itemName: Array<String>
lateinit var itemQuantity: Array<String>
lateinit var itemId: Array<Int>
/**
 * A simple [Fragment] subclass.
 * Use the [Item.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_item, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Item.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        itemData()
        val layoutManager = LinearLayoutManager(context)
        newRecyclerView = view.findViewById(R.id.recyclerViewItem)
        newRecyclerView.layoutManager = layoutManager
        newRecyclerView.setHasFixedSize(true)
        itemDataAdapter = ItemDataAdapter(arrayList)
        newRecyclerView.adapter = itemDataAdapter
    }

    private fun itemData(){

        itemId = arrayOf(1, 2)
        itemName = arrayOf("Apel", "Jagung")
        itemQuantity = arrayOf("x1", "x2")

        arrayList = arrayListOf<ItemData>()

        for(i in itemId.indices){
            val itemData = ItemData(itemName[i], itemQuantity[i])
            arrayList.add(itemData)
        }
    }
}