package com.example.to_do.ui.shareTask

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.to_do.R


/**
 * A simple [Fragment] subclass.
 * Use the [ShareTaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ShareTaskFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_task, container, false)
    }

}