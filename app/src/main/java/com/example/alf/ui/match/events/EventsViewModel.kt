package com.example.alf.ui.match.events

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class EventsViewModel(private val matchId: Int) : ViewModel() {

    private val eventApiService: EventApiService = EventApiService()

    private val eventsResourceLiveData = MutableLiveData<Resource<List<Event>>>()
    val eventsLiveData = Transformations.map(eventsResourceLiveData) { resource -> resource.data }
    private val eventsLoadingLiveData = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Loading }
    val eventsErrorLiveData = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(eventsLiveData) { it != null && it.isEmpty() }

    val deleteEventActionLiveData = MutableLiveData<ViewEvent<Int>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

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
        eventApiService.fetchMatchEvents(
                matchId,
                { eventsResourceLiveData.value = Resource.Success(it) },
                { eventsResourceLiveData.value = Resource.Error(it) }
        )
    }

    fun deleteEvent(event: Event, position: Int) {
        loadingInProgressLiveData.value = true
        eventApiService.deleteEvent(event) {
            deleteEventActionLiveData.value = if (it) ViewEvent(position) else ViewEvent(-1)
        }
    }

}
