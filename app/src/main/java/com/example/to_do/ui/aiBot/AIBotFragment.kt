package com.example.to_do.ui.aiBot
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.to_do.R
import com.example.to_do.databinding.FragmentAibotBinding
import com.google.ai.client.generativeai.GenerativeModel
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class AIBotFragment : Fragment()
{

    private var _binding: FragmentAibotBinding? = null
    private val binding get() = _binding!!
    private lateinit var txtResponse: TextView
    private lateinit var idTVQuestion :TextView

    private lateinit var etQuestion: TextInputEditText

    override  fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAibotBinding.inflate(inflater, container, false)
        txtResponse = binding.txtResponse
        idTVQuestion = binding.idTVQuestion
        etQuestion = binding.etQuestion

        var question: String
        val clearButton:Button = binding.clear

        etQuestion.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEND) {

                // setting response tv on below line.
                txtResponse.text = resources.getString(R.string.loaderText)

                // validating text
                question = etQuestion.text.toString().trim()

                Toast.makeText(requireContext(), question, Toast.LENGTH_SHORT).show()
                if (question.isNotEmpty()) {
                    main(question) { response ->
                      activity?.runOnUiThread{
                            val questionSpannable =  SpannableString(question)
                            questionSpannable.setSpan(UnderlineSpan(),0,questionSpannable.length,0)
                            idTVQuestion.text = questionSpannable
                            txtResponse.text = response
                            etQuestion.text = null
                        }
                    }
                }
                return@OnEditorActionListener true
            }
            false
        })

        clearButton.setOnClickListener{
            idTVQuestion.text= getString(R.string.question)
            txtResponse.text="I am your answer"
        }


        return binding.root
    }

    private fun main(question: String, param: (String) -> Unit?)
    {
        val generativeModel = GenerativeModel(
            //modelType
            modelName = "gemini-pro",
            //apIKey
            apiKey = "AIzaSyBES9a2EF7Syiy9FUZJcHP41yn87gUQaSc"
        )
        MainScope().launch {
            val response  = generativeModel.generateContent(question)
            param(response.text.toString())
            print(response.text)
        }
    }
}

