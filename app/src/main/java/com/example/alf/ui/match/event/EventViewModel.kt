package com.example.alf.ui.match.event

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.ui.common.ViewEvent

class EventViewModel(val matchId: Int, val eventId: Int, event: Event) : ViewModel() {

    private val eventService: EventApiService = EventApiService()

    var eventLiveData = MutableLiveData(event)
    //var saveEnabledLiveData = Transformations.map(eventLiveData) { li -> li != null }

    val updateEventResultLiveData = MutableLiveData<Boolean?>()
    val deleteEventResultLiveData = MutableLiveData<ViewEvent<Boolean>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

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
        /*loadingInProgressLiveData.value = true
        eventService.deleteEvent(deleteEventResultLiveData, eventLiveData.value!!)*/
    }

}