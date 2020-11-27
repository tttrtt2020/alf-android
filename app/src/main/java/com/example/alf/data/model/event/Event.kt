package com.example.alf.data.model.event

import com.example.alf.data.model.Team

data class Event(
    var minute: String,
    var name: String,
    var eventType: EventType,
    var team: Team
)