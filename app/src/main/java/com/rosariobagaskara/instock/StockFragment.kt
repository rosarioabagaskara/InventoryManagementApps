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

private lateinit var stockDataAdapter : StockDataAdapter
private lateinit var newRecyclerView : RecyclerView
lateinit var addStock: FloatingActionButton
/**
 * A simple [Fragment] subclass.
 * Use the [Item.newInstance] factory method to
 * create an instance of this fragment.
 */
class StockFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_stock, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Stock.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            StockFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Move to AddStockActivity
        addStock = view.findViewById(R.id.addStock)
        addStock.setOnClickListener {
            val intent = Intent(activity, AddStockActivity::class.java)
            activity?.startActivity(intent)
        }

        if(getItemList().size > 0 ){
            //get arrayList value and show the value in recycleView
            val layoutManager = LinearLayoutManager(context)
            newRecyclerView = view.findViewById(R.id.recyclerViewStock)
            newRecyclerView.visibility = View.VISIBLE
            newRecyclerView.layoutManager = layoutManager
            newRecyclerView.setHasFixedSize(true)
            stockDataAdapter = StockDataAdapter(getItemList())
            newRecyclerView.adapter = stockDataAdapter
        }else{
            newRecyclerView.visibility = View.GONE
        }

    }

    private fun getItemList(): ArrayList<StockData>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(requireContext())

        val itemStokList : ArrayList<StockData> = databaseHandler.viewItemStock()

        return  itemStokList
    }
}