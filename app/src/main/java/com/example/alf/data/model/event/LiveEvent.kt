package com.example.alf.data.model.event

data class LiveEvent(
        var id: Int,
        var name: String,
        var events: List<Event>
)