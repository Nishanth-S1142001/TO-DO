package com.example.to_do.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.to_do.R
import com.example.to_do.databinding.FragmentMonthlyCalendarBinding
import com.example.to_do.databinding.FragmentWeeklyCalendarBinding

class WeeklyCalendarFragment : Fragment() {

    private var _binding: FragmentWeeklyCalendarBinding? = null
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
        // Inflate the layout for this fragment
        return binding.root
    }

 
}