package com.example.alf.ui.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MatchViewModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MatchViewModel(id) as T
    }
}