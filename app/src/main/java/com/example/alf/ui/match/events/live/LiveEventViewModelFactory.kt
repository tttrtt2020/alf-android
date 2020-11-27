package com.example.alf.ui.match.events.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class LiveEventViewModelFactory(private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LiveEventViewModel(id) as T
    }
}