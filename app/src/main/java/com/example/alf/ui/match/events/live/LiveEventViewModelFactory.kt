package com.example.alf.ui.match.events.live

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alf.data.model.event.LiveEventType


class LiveEventViewModelFactory(private val id: Int, private val liveEventType: LiveEventType) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LiveEventViewModel(id, liveEventType) as T
    }
}