package com.example.alf.ui.match.events.live

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.event.LiveEventType
import com.example.alf.data.repository.LiveEventTypeApiService

class LiveEventTypesViewModel : ViewModel() {

    private var liveEventTypeApiService: LiveEventTypeApiService = LiveEventTypeApiService()

    val liveEventTypesLiveData: MutableLiveData<List<LiveEventType>> = MutableLiveData()

    init {
        liveEventTypeApiService.fetchLiveEventTypes(liveEventTypesLiveData)
    }

}
