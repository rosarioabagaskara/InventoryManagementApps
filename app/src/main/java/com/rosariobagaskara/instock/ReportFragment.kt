package com.rosariobagaskara.instock

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONObject
import java.text.DecimalFormat
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

private lateinit var pendapatanGalonAdapter: PendapatanGalonAdapter
private lateinit var pendapatanLaundryAdapter: PendapatanLaundryAdapter
private lateinit var logPenjualanStokAdapter: LogPenjualanStokAdapter
private lateinit var newGalonRecyclerView : RecyclerView
private lateinit var newLaundryRecyclerView : RecyclerView
private lateinit var newLogStokRecyclerView : RecyclerView
lateinit var hargaTotal: TextView
lateinit var totalLiter: TextView
lateinit var dateRangePicker: ImageView

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
        newGalonRecyclerView = view.findViewById(R.id.pendapatanGalonRecyclerView)
        newLaundryRecyclerView = view.findViewById(R.id.pendapatanLaundryRecyclerView)
        newLogStokRecyclerView = view.findViewById(R.id.penjualanStokRecyclerView)

        var datePickerHash = hashMapOf(
        "firstDate" to  LocalDateTime.now().format(DateTimeFormatter.ISO_DATE),
        "secondDate" to LocalDateTime.now().format(DateTimeFormatter.ISO_DATE))
        var hargaTemp = 0.0
        var literTemp = 0
        var orderList = getOrderListBySuccessStatusAndDate(datePickerHash["firstDate"].toString(), datePickerHash["secondDate"].toString())
        var logStokList = getLogPenjualanStok(datePickerHash["firstDate"].toString(), datePickerHash["secondDate"].toString())
        hargaTotal = view.findViewById(R.id.hargaTotalTextView)
        totalLiter = view.findViewById(R.id.totalLiterGalonTextView)
        dateRangePicker = view.findViewById(R.id.calendarRangeImageView)
        dateRangePicker.setOnClickListener {
            val supportFragmentManager = requireActivity().supportFragmentManager
            val dpd = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Pilih Range Tanggal").build()
            var firstDate : String = ""
            var secondDate : String = ""

            // show
            dpd.show(supportFragmentManager, "DatePickerFragment")

            dpd.addOnPositiveButtonClickListener { selectedDate ->
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                firstDate = sdf.format(selectedDate.first).toString()
                secondDate = sdf.format(selectedDate.second).toString()

                datePickerHash = hashMapOf(
                    "firstDate" to  firstDate,
                    "secondDate" to secondDate)
                orderList = getOrderListBySuccessStatusAndDate(datePickerHash["firstDate"].toString(), datePickerHash["secondDate"].toString())
                logStokList = getLogPenjualanStok(datePickerHash["firstDate"].toString(), datePickerHash["secondDate"].toString())
            }
        }

        if(orderList.size > 0){
            val layoutManager = LinearLayoutManager(context)
            val layoutManagerLaundry = LinearLayoutManager(context)
            for(value in orderList){
                val JsonProdukHashMap = JSONObject(value.orderProduk as Map<String, Map<String, String>>)
                val orderHashMap = Json.decodeFromString<Map<String, Map<String, String>>>(JsonProdukHashMap.toString())
                for (i in 0 until orderHashMap.size){
                    val index = orderHashMap[i.toString()]
                    if (index != null) {
                        hargaTemp += index["HargaProduk"]?.toDouble()!!
                        literTemp += index["Liter"]?.toInt()!!
                    }
                }
            }
            val formatter: NumberFormat = DecimalFormat("#,###")
            val formattedNumber = formatter.format(hargaTemp)
            hargaTotal.text = "Rp. $formattedNumber"
            totalLiter.text = "Total Liter Galon: $literTemp Liter"
            newGalonRecyclerView.visibility = View.VISIBLE
            newGalonRecyclerView.layoutManager = layoutManager
            newGalonRecyclerView.setHasFixedSize(true)
            pendapatanGalonAdapter = PendapatanGalonAdapter(orderList)
            newGalonRecyclerView.adapter = pendapatanGalonAdapter

            newLaundryRecyclerView.visibility = View.VISIBLE
            newLaundryRecyclerView.layoutManager = layoutManagerLaundry
            newLaundryRecyclerView.setHasFixedSize(true)
            pendapatanLaundryAdapter = PendapatanLaundryAdapter(orderList)
            newLaundryRecyclerView.adapter = pendapatanLaundryAdapter
        }else{
            newGalonRecyclerView.visibility = View.GONE
            newLaundryRecyclerView.visibility = View.GONE
        }
        if(logStokList.size > 0){
            val layoutManagerLog = LinearLayoutManager(context)
            newLogStokRecyclerView.visibility = View.VISIBLE
            newLogStokRecyclerView.layoutManager = layoutManagerLog
            newLogStokRecyclerView.setHasFixedSize(true)
            logPenjualanStokAdapter = LogPenjualanStokAdapter(logStokList)
            newLogStokRecyclerView.adapter = logPenjualanStokAdapter
        }else{
            newLogStokRecyclerView.visibility = View.GONE
        }
    }

    private fun getOrderListBySuccessStatusAndDate(date1 : String, date2: String) : ArrayList<OrderData>{
        val databaseHandler = DatabaseHandler(requireContext())
        val orderList = databaseHandler.getOrderByStatusAndDate("Success", date1, date2)
        return orderList
    }

    private fun getLogPenjualanStok(date1 : String, date2: String) : ArrayList<LogStokData>{
        val databaseHandler = DatabaseHandler(requireContext())
        val logStokList = databaseHandler.getLogStockNameAndSumByOrderDate(date1, date2)
        return logStokList
    }
}