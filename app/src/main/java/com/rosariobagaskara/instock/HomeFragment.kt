package com.rosariobagaskara.instock

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var orderAdapter : OrderDataAdapter
private lateinit var newRecyclerView : RecyclerView
private lateinit var arrayList: ArrayList<OrderData>
lateinit var orderNumber: Array<String>
lateinit var orderDate: Array<String>
lateinit var namaPemesan: Array<String>
lateinit var item: Array<String>
lateinit var itemNumber: Array<String>

/**
 * A simple [Fragment] subclass.
 * Use the [home.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment home.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderData()
        val layoutManager = LinearLayoutManager(context)
        newRecyclerView = view.findViewById(R.id.recyclerView)
        newRecyclerView.layoutManager = layoutManager
        newRecyclerView.setHasFixedSize(true)
        orderAdapter = OrderDataAdapter(arrayList)
        newRecyclerView.adapter = orderAdapter
    }

    private fun orderData(){

        orderNumber = arrayOf("Order #1","Order #2","Order #3","Order #4","Order #5","Order #6","Order #7","Order #8", "Order #9","Order #10","Order #11","Order #12","Order #13","Order #14","Order #15","Order #16")
        orderDate = arrayOf("02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022","02/06/2022","02/07/2022","02/08/2022","02/05/2022")
        namaPemesan = arrayOf("Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha","Bagas","Tegar","Abram","Gretha")
        item = arrayOf("Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon","Galon", "Laundry Biasa","Galon","Galon")
        itemNumber = arrayOf("x1", "x1","x2","x4","x1", "x1","x2","x4","x1", "x1","x2","x4","x1", "x1","x2","x4")


        arrayList = arrayListOf<OrderData>()

        for(i in orderNumber.indices){
            val orderData = OrderData(orderNumber[i], orderDate[i], namaPemesan[i], item[i], itemNumber[i])
            arrayList.add(orderData)
        }
    }
}