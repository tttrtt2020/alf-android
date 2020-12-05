package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider
import com.example.alf.data.model.Person


class PersonViewModelFactory(
        private val application: Application,
        private val personId: Int,
        private val person: Person?
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonViewModel(application, personId, person) as T
    }
}