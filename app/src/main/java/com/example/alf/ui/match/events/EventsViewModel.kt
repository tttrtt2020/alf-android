package com.example.alf.ui.match.events

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService

class EventsViewModel(val matchId: Int) : ViewModel() {

    private var eventApiService: EventApiService = EventApiService()

    var eventsLiveData: MutableLiveData<List<Event>?> = MutableLiveData()

    var getEventsResultLiveData: MutableLiveData<Boolean?> = Transformations.map(eventsLiveData) { es -> es != null } as MutableLiveData<Boolean?>

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
        //eventsLiveData.value = null
        eventApiService.fetchMatchEvents(eventsLiveData, matchId)
    }

    fun deleteEvent(event: Event) {
        eventApiService.deleteEvent(deleteEventLiveData, event)
    }

}
