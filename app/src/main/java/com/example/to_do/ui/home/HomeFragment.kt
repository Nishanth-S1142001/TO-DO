package com.example.to_do.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.fragment.app.Fragment

import androidx.navigation.Navigation

import com.example.to_do.R
import com.example.to_do.databinding.FragmentHomeBinding

import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {


    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {



        _binding = FragmentHomeBinding.inflate(inflater, container, false)
     //   val viewHome: View = inflater.inflate(R.layout.fragment_home, container, false)
       binding.fabHome.setOnClickListener{ viewHome ->
            Snackbar.make(viewHome, "Do not forget to set a reminder to keep up with your task!", Snackbar.LENGTH_LONG)
                //         .setAction("Action", null)
                .setAnchorView(R.id.fabHome).show()
                Navigation.findNavController(viewHome).navigate(R.id.nav_reminder)

        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}