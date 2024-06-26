package com.example.to_do.ui.feedback

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.to_do.R
import com.example.to_do.databinding.FragmentAddreminderBinding

import com.example.to_do.databinding.FragmentFeedbackBinding


class FeedbackFragment : Fragment() {

    private var _binding: FragmentFeedbackBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        //button click to get input and call sendEmail method
        binding.composeEmail.setOnClickListener {
            //get input from EditTexts and save in variables
            val recipient = binding.toEt.text.toString().trim()
            val subject = binding.subjectEt.text.toString().trim()
            val message = binding.bodyEt.text.toString().trim()

            //method call for email intent with these inputs as parameters
            sendEmail(recipient, subject, message)
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun sendEmail(recipient: String, subject: String, message: String)
    {
        val mIntent = Intent(Intent.ACTION_SEND)
        /*To send an email you need to specify mailto: as URI using setData() method
        and data type will be to text/plain using setType() method*/
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        // put recipient email in intent
        /* recipient is put as array because you may wanna send email to multiple emails
           so enter comma(,) separated emails, it will be stored in array*/
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        //put the Subject in the intent
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        //put the message in the intent
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            //if any thing goes wrong for example no email client application or any exception
            //get and show exception message
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_LONG).show()
        }

    }

}


