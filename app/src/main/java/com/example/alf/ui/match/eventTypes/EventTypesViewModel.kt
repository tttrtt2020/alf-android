package com.example.alf.ui.match.eventTypes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.event.EventType
import com.example.alf.data.repository.EventTypeApiService

class EventTypesViewModel : ViewModel() {

    private var eventTypeApiService: EventTypeApiService = EventTypeApiService()

    val eventTypesLiveData: MutableLiveData<List<EventType>> = MutableLiveData()

    init {
        eventTypeApiService.fetchEventTypes(eventTypesLiveData)
    }

}
