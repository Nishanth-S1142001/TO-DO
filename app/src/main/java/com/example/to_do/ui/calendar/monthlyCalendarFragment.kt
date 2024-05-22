package com.example.to_do.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.toColor
import androidx.navigation.Navigation
import com.example.to_do.R
import com.example.to_do.databinding.FragmentMonthlyCalendarBinding


class MonthlyCalendarFragment : Fragment() {
    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var _binding: FragmentMonthlyCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentMonthlyCalendarBinding.inflate(inflater, container, false)
        binding.monthlyCalendarView.dateTextAppearance.toColor()
        binding.monthlyCalendarView.setOnDateChangeListener { _, i, i1, i2 ->

            year = i
            month = i1 + 1
            day = i2

        }

        binding.fabHome.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_home)

        }
        binding.fabDaily.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_dailyCalendar)

        }
        binding.fabWeekly.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_weeklyCalendar)

        }
        return binding.root
    }
}