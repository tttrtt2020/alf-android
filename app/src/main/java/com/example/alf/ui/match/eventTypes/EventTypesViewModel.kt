package com.example.alf.ui.match.eventTypes

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.event.EventType
import com.example.alf.data.repository.EventTypeApiService

class EventTypesViewModel(private val matchId: Int) : ViewModel() {

    private var eventTypeApiService: EventTypeApiService = EventTypeApiService()

    val eventTypesLiveData: MutableLiveData<List<EventType>?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventTypesLiveData) {
            loadingInProgressLiveData.value = false
        }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && eventTypesLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(eventTypesLiveData) { update() }

            update()
        }

        getEventTypes()
    }

    private fun getEventTypes() {
        loadingInProgressLiveData.value = true
        eventTypeApiService.fetchMatchEventTypes(eventTypesLiveData, matchId)
    }

}
