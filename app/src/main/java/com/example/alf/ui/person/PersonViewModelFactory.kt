package com.example.alf.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alf.data.model.Person


class PersonViewModelFactory(
        private val personId: Int,
        private val person: Person?
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return PersonViewModel(personId, person) as T
    }
}