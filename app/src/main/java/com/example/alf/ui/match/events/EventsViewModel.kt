package com.example.alf.ui.match.events

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService

class EventsViewModel(application: Application, id: Int) : AndroidViewModel(application) {

    private var eventApiService: EventApiService = EventApiService()

    var eventsLiveData: MutableLiveData<List<Event>> = MutableLiveData()

    var getEventsResultLiveData: MutableLiveData<Boolean?> = Transformations.map(eventsLiveData) { es -> es != null } as MutableLiveData<Boolean?>

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventsLiveData) { loadingInProgressLiveData.value = false }

        getEvents(id)
    }

    private fun getEvents(id: Int) {
        loadingInProgressLiveData.value = true
        //eventsLiveData.value = null
        eventApiService.fetchMatchEvents(eventsLiveData, id)
        eventsLiveData.value = ArrayList()
    }

}
