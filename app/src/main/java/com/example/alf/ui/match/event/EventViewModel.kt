package com.example.alf.ui.match.event

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService

class EventViewModel(val matchId: Int, val eventId: Int, event: Event) : ViewModel() {

    private var eventService: EventApiService = EventApiService()

    var eventLiveData: MutableLiveData<Event> = MutableLiveData(event)
    //var saveEnabledLiveData: LiveData<Boolean> = Transformations.map(eventLiveData) { li -> li != null }

    var updateEventResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    var deleteEventResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            addSource(eventLiveData) { loadingInProgressLiveData.value = false }
            addSource(updateEventResultLiveData) { loadingInProgressLiveData.value = false }
            addSource(deleteEventResultLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun saveEvent() {
        if (eventLiveData.value!!.id == 0) {
            //createEvent()
        } else {
            updateEvent()
        }
    }

    fun updateEvent() {
        loadingInProgressLiveData.value = true
        eventService.updateMatchEvent(updateEventResultLiveData, matchId, eventLiveData.value!!)
    }

    fun deleteEvent() {
        loadingInProgressLiveData.value = true
        eventService.deleteEvent(deleteEventResultLiveData, eventLiveData.value!!)
    }

}