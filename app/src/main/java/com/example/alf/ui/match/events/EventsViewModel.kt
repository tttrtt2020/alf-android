package com.example.alf.ui.match.events

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class EventsViewModel(private val matchId: Int) : ViewModel() {

    private var eventApiService: EventApiService = EventApiService()

    var eventsResourceLiveData: MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    var eventsLiveData: LiveData<List<Event>?> = Transformations.map(eventsResourceLiveData) { resource -> resource.data }
    var eventsLoadingLiveData: LiveData<Boolean> = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Loading }
    var eventsErrorLiveData: LiveData<Boolean> = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Error }

    var emptyCollectionLiveData: LiveData<Boolean> = Transformations.map(eventsLiveData) { it != null && it.isEmpty() }

    var deleteEventActionLiveData: MutableLiveData<ViewEvent<Int>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventsLoadingLiveData) { loadingInProgressLiveData.value = it }
        loadingInProgressLiveData.addSource(deleteEventActionLiveData) { loadingInProgressLiveData.value = false }

        getEvents()
    }

    fun reset() {
        eventsResourceLiveData.value = eventsResourceLiveData.value
    }

    fun getEvents() {
        eventsResourceLiveData.value = Resource.Loading()
        eventApiService.fetchMatchEvents(eventsResourceLiveData, matchId)
    }

    fun deleteEvent(event: Event, position: Int) {
        loadingInProgressLiveData.value = true
        eventApiService.deleteEvent(event) {
            deleteEventActionLiveData.value = if (it) ViewEvent(position) else ViewEvent(-1)
        }
    }

}
