package com.rosariobagaskara.instock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var pendapatanAdapter: PendapatanAdapter
private lateinit var newRecyclerView : RecyclerView
lateinit var hargaTotal: TextView

/**
 * A simple [Fragment] subclass.
 * Use the [ReportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ReportFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_report, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Report.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newRecyclerView = view.findViewById(R.id.pendapatanRecyclerView)
        val currentDate = LocalDateTime.now()
        Log.e("tes", currentDate.toString())
        val currentDateString = currentDate.format(DateTimeFormatter.ISO_DATE)
        val orderList = getOrderListByDate(currentDateString)
        var hargaTemp = 0.0
        hargaTotal = view.findViewById(R.id.hargaTotalTextView)
        if(orderList.size > 0){
            val layoutManager = LinearLayoutManager(context)
            for(value in orderList){
                val JsonProdukHashMap = JSONObject(value.orderProduk as Map<String, Map<String, String>>)
                val orderHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
                for (i in 0 until orderHashMap.size){
                    val index = orderHashMap[i.toString()]
                    if (index != null) {
                        val formatter: NumberFormat = DecimalFormat("#,###")
                        val myNumber = index["HargaProduk"]?.toDouble()
                        val formattedNumber = formatter.format(myNumber)
                        hargaTemp += myNumber!!
                    }
                }
            }
            val formatter: NumberFormat = DecimalFormat("#,###")
            val formattedNumber = formatter.format(hargaTemp)
            hargaTotal.text = "Rp. $formattedNumber"
            newRecyclerView.visibility = View.VISIBLE
            newRecyclerView.layoutManager = layoutManager
            newRecyclerView.setHasFixedSize(true)
            pendapatanAdapter = PendapatanAdapter(orderList)
            newRecyclerView.adapter = pendapatanAdapter
        }else{
            newRecyclerView.visibility = View.GONE
        }
    }

    private fun getOrderListByDate(date : String) : ArrayList<OrderData>{
        val databaseHandler = DatabaseHandler(requireContext())
        val orderList = databaseHandler.getOrderByDate(date)
        return orderList
    }
}