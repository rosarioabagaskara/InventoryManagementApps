package com.rosariobagaskara.instock

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var orderAdapter : OrderDataAdapter
private lateinit var newRecyclerView : RecyclerView
lateinit var addOrderButton: FloatingActionButton

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

        addOrderButton = view.findViewById(R.id.addOrder)
        addOrderButton.setOnClickListener {
            val intent = Intent(activity, AddOrderActivity::class.java)
            startActivity(intent)
        }
        newRecyclerView = view.findViewById(R.id.recyclerView)

        if(getOrderList().size > 0){
            //get arrayList value and show the value in recycleView
            val layoutManager = LinearLayoutManager(context)
            newRecyclerView.visibility = View.VISIBLE
            newRecyclerView.layoutManager = layoutManager
            newRecyclerView.setHasFixedSize(true)
            orderAdapter = context?.let { OrderDataAdapter(it, getOrderList()) }!!
            newRecyclerView.adapter = orderAdapter
        }else{
            newRecyclerView.visibility = View.GONE
        }

    }

    private fun getOrderList(): ArrayList<OrderData>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())
        val orderList : ArrayList<OrderData> = databaseHandler.viewOrder()
        return  orderList
    }
}