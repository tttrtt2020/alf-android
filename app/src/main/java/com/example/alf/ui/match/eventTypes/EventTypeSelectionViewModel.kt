package com.example.alf.ui.match.eventTypes

import androidx.lifecycle.*
import com.example.alf.data.model.event.EventType
import com.example.alf.data.repository.EventTypeApiService
import com.example.alf.network.Resource

class EventTypeSelectionViewModel(private val matchId: Int) : ViewModel() {

    private val eventTypeApiService: EventTypeApiService = EventTypeApiService()

    private val eventTypesResourceLiveData = MutableLiveData<Resource<List<EventType>>>()
    val eventTypesLiveData = Transformations.map(eventTypesResourceLiveData) { resource -> resource.data }
    private val eventTypesLoadingLiveData = Transformations.map(eventTypesResourceLiveData) { resource -> resource is Resource.Loading }
    val eventTypesErrorLiveData = Transformations.map(eventTypesResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(eventTypesLiveData) { it != null && it.isEmpty() }

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventTypesLoadingLiveData) { loadingInProgressLiveData.value = it }

        getEventTypes()
    }

    fun getEventTypes() {
        loadingInProgressLiveData.value = true
        eventTypesResourceLiveData.value = Resource.Loading()
        eventTypeApiService.fetchMatchEventTypes(
                matchId,
                { eventTypesResourceLiveData.value = Resource.Success(it) },
                { eventTypesResourceLiveData.value = Resource.Error(it) }
        )
    }

}
