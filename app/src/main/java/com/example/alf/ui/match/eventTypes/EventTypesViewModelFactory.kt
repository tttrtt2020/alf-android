package com.example.alf.ui.match.eventTypes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class EventTypesViewModelFactory(
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventTypesViewModel(matchId) as T
    }
}