package com.example.to_do.ui.favTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.to_do.R




/**
 * A simple [Fragment] subclass.
 * Use the [FavTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavTaskFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fav_task, container, false)
    }

}