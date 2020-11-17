package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class PersonViewModelFactory(private val application: Application, private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonViewModel(application, id) as T
    }
}