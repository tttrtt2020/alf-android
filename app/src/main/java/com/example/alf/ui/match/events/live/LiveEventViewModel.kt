package com.example.alf.ui.match.events.live

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.event.Event

class LiveEventViewModel(id: Int) : ViewModel() {

    var eventLiveData: LiveData<Event> = MutableLiveData()
    var nameLiveData: LiveData<String> = Transformations.map(eventLiveData) { e -> e.name }
    var hostTeamLabelLiveData: LiveData<String> = Transformations.map(eventLiveData) { e -> e.team.name }
    var guestTeamLiveData: LiveData<String> = Transformations.map(eventLiveData) { e -> e.team.name }

    fun saveLiveEvent() {
        TODO("Not yet implemented")
    }

}