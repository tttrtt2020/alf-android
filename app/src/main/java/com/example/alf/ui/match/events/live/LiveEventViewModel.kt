package com.example.alf.ui.match.events.live

import androidx.lifecycle.*
import com.example.alf.data.model.Person
import com.example.alf.data.model.Team
import com.example.alf.data.model.event.LiveEvent
import com.example.alf.data.model.event.LiveEventType

class LiveEventViewModel(id: Int, liveEventType: LiveEventType) : ViewModel() {

    var liveEventLiveData: MediatorLiveData<LiveEvent> = MediatorLiveData()
    var nameLiveData: LiveData<String> = MutableLiveData(liveEventType.name)
    var minuteLiveData: MutableLiveData<String> = MutableLiveData()
    var teamLiveData: MutableLiveData<Team> = MutableLiveData()
    var personLiveData: MutableLiveData<Person> = MutableLiveData()
    /*var personNameLiveData: LiveData<String> = Transformations.map(personLiveData) { p ->
        p.lastName.plus(" ".plus(p.firstName)).plus(" ".plus(p.patronymic))
    }*/
    var personNameLiveData: MutableLiveData<String> = MutableLiveData()
    var saveEnabledLiveData: LiveData<Boolean> = Transformations.map(liveEventLiveData) { li -> li != null }

    init {
        liveEventLiveData.value = null

        liveEventLiveData.apply {
            fun update() {
                val minute = minuteLiveData.value
                val team = teamLiveData.value
                val person = personLiveData.value

                value = if (minute.isNullOrEmpty() || team == null || person == null) {
                    null
                } else {
                    LiveEvent(0, liveEventType.name, liveEventType, minute.toInt(), team, person, ArrayList())
                }
            }

            addSource(minuteLiveData) { update() }
            addSource(teamLiveData) { update() }
            addSource(personLiveData) { update() }

            update()
        }
    }

    fun saveLiveEvent() {
        TODO("Not yet implemented")
    }

}