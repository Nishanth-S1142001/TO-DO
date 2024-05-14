package com.example.to_do.ui.dummy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class dummyViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dummy Fragment"
    }
    val text: LiveData<String> = _text
}