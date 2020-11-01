package com.example.alf.ui.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PersonsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is persons Fragment"
    }
    val text: LiveData<String> = _text
}