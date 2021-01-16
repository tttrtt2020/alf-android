package com.example.alf.ui.match.eventTypes

import androidx.lifecycle.*
import com.example.alf.data.model.event.EventType
import com.example.alf.data.repository.EventTypeApiService
import com.example.alf.network.Resource

class EventTypeSelectionViewModel(private val matchId: Int) : ViewModel() {

    private var eventTypeApiService: EventTypeApiService = EventTypeApiService()

    private val eventTypesResourceLiveData: MutableLiveData<Resource<List<EventType>>> = MutableLiveData()
    val eventTypesLiveData: LiveData<List<EventType>?> = Transformations.map(eventTypesResourceLiveData) { resource -> resource.data }
    private val eventTypesLoadingLiveData: LiveData<Boolean> = Transformations.map(eventTypesResourceLiveData) { resource -> resource is Resource.Loading }
    val eventTypesErrorLiveData: LiveData<Boolean> = Transformations.map(eventTypesResourceLiveData) { resource -> resource is Resource.Error }

    var emptyCollectionLiveData: LiveData<Boolean> = Transformations.map(eventTypesLiveData) { it != null && it.isEmpty() }

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

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
