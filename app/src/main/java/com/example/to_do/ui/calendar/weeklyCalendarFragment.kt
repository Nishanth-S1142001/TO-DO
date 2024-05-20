package com.example.to_do.ui.calendar

import android.app.AlertDialog
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import com.example.to_do.R

import com.example.to_do.databinding.FragmentWeeklyCalendarBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class WeeklyCalendarFragment : Fragment(), CalendarAdapter.CalendarInterface {

    companion object
    {
        private val TAG = "WeeklyCalendarFragment"
    }


    private var _binding: FragmentWeeklyCalendarBinding? = null
    private var sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(
        Locale.ENGLISH)
    private var mDate : Date? = null
    private val calendarAdapter = CalendarAdapter(this, arrayListOf())
    private val calendarList  = ArrayList<CalendarData>()
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentWeeklyCalendarBinding.inflate(inflater, container, false)
        binding.fabHome.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_home)

        }
        binding.fabDaily.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_dailyCalendar)

        }
        binding.fabMonthly.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_monthlyCalendar)

        }

        init()
        initCalendar()
        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initCalendar() {
        mDate = Date()
        cal.time = Date()
        getDates()
    }

    private fun init() {
        binding.apply {
            weeklyPicker.text = sdf.format(cal.time)
            calendarView.setHasFixedSize(true)
            calendarView.adapter = calendarAdapter
            weeklyPicker.setOnClickListener {
                displayDatePicker()
            }
        }
    }

    private fun displayDatePicker() {
         val materialDateBuilder: MaterialDatePicker.Builder<Long> = MaterialDatePicker.Builder.datePicker()
        materialDateBuilder.setTitleText("Select Date")
        materialDateBuilder.setTheme(R.style.MaterialCalendarTheme)
        val materialDatePicker = materialDateBuilder.build()

        materialDatePicker.show(parentFragmentManager,"MATERIAL_DATE_PICKER")
        materialDatePicker.addOnPositiveButtonClickListener {
            try {
                mDate = Date(it)
                binding.weeklyPicker.text = sdf.format(it)
                cal.time = Date(it)
                getDates()
            }
            catch (e:ParseException)
            {
                Log.e(TAG,"displayDatePicker : ,${e.message}")
            }
        }
    }

    private fun getDates() {
        val dateList =  ArrayList<CalendarData>()
        val dates = ArrayList<Date>()
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)
        monthCalendar.set(Calendar.DAY_OF_MONTH,1)

        while(dates.size < maxDaysInMonth)
        {
            dates.add(monthCalendar.time)
            dateList.add(CalendarData(monthCalendar.time))
            monthCalendar.add(Calendar.DAY_OF_MONTH,1)
        }
        calendarList.clear()
        calendarList.addAll(dateList)
        calendarAdapter.updateList(dateList)

        for(item in dateList.indices)
        {
            if(dateList[item].data==mDate)
            {
                calendarAdapter.setPosition(item)
                binding.calendarView.scrollToPosition(item)
            }
        }
    }

    override fun onSelect(calendarData: CalendarData, position: Int, day: TextView) {
       calendarList.forEachIndexed{
                                  index, calendarData ->
           calendarData.isSelected = index == position

       }
        calendarAdapter.updateList(calendarList)
    }

}