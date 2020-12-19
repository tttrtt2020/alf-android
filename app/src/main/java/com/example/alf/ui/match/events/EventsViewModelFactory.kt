package com.example.alf.ui.match.events

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class EventsViewModelFactory(
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventsViewModel(matchId) as T
    }
}