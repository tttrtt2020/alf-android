package com.example.alf.ui.match.event

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alf.data.model.event.Event


class EventViewModelFactory(
        private val matchId: Int,
        private val eventId: Int,
        private val event: Event
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventViewModel(matchId, eventId, event) as T
    }
}