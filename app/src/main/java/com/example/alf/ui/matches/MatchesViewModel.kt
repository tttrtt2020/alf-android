package com.example.alf.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MatchesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is matches Fragment"
    }
    val text: LiveData<String> = _text
}