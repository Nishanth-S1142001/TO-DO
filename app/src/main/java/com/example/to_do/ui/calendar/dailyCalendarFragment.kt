package com.example.to_do.ui.calendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.to_do.R
import com.example.to_do.databinding.FragmentDailyCalendarBinding


class DailyCalendarFragment : Fragment() {

    private var _binding: FragmentDailyCalendarBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyCalendarBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.fabHome.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_home)

        }
        binding.fabMonthly.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_monthlyCalendar)

        }
        binding.fabWeekly.setOnClickListener{ viewHome ->

            Navigation.findNavController(viewHome).navigate(R.id.nav_weeklyCalendar)

        }
        return binding.root

    }

    

}