package com.example.alf.data.model.event

import com.example.alf.data.model.Person
import com.example.alf.data.model.Team

data class LiveEvent(
        var id: Int,
        var name: String,
        var liveEventType: LiveEventType,
        var minute: Int,
        var team: Team,
        var person: Person,
        var events: List<Event>
)