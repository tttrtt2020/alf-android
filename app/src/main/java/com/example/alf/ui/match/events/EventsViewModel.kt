package com.example.alf.ui.match.events

import androidx.lifecycle.*
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService

class EventsViewModel(matchId: Int) : ViewModel() {

    private var eventApiService: EventApiService = EventApiService()

    var eventsLiveData: MutableLiveData<List<Event>> = MutableLiveData()

    var getEventsResultLiveData: MutableLiveData<Boolean?> = Transformations.map(eventsLiveData) { es -> es != null } as MutableLiveData<Boolean?>

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(eventsLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && eventsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(eventsLiveData) { update() }

            update()
        }

        getEvents(matchId)
    }

    private fun getEvents(id: Int) {
        loadingInProgressLiveData.value = true
        //eventsLiveData.value = null
        eventApiService.fetchMatchEvents(eventsLiveData, id)
    }

}
