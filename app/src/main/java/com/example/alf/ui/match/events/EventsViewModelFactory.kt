package com.example.alf.ui.match.events

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class EventsViewModelFactory(
        private val application: Application,
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return EventsViewModel(application, matchId) as T
    }
}