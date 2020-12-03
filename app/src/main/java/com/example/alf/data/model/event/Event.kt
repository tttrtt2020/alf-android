package com.example.alf.data.model.event

import com.example.alf.data.model.Person
import com.example.alf.data.model.Team
import java.io.Serializable

data class Event(
        var id: Int,
        var team: Team,
        var person: Person,
        var minute: String,
        var eventType: EventType,
        var notice: String?,
        var description: String?,
) : Serializable