package com.example.alf.ui.match.events

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.network.Resource

class EventsViewModel(private val matchId: Int) : ViewModel() {

    private var eventApiService: EventApiService = EventApiService()

    var eventsResourceLiveData: MutableLiveData<Resource<List<Event>>> = MutableLiveData()
    var eventsLiveData: LiveData<List<Event>?> = Transformations.map(eventsResourceLiveData) { resource -> resource.data }
    var eventsLoadingLiveData: LiveData<Boolean> = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Loading }
    var eventsErrorLiveData: LiveData<Boolean> = Transformations.map(eventsResourceLiveData) { resource -> resource is Resource.Error }

    var deleteEventLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventsLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(deleteEventLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && eventsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(eventsLiveData) { update() }

            update()
        }

        getEvents()
    }

    fun getEvents() {
        loadingInProgressLiveData.value = true
        eventApiService.fetchMatchEvents(eventsResourceLiveData, matchId)
    }

    fun deleteEvent(event: Event) {
        loadingInProgressLiveData.value = true
        eventApiService.deleteEvent(deleteEventLiveData, event)
    }

}
