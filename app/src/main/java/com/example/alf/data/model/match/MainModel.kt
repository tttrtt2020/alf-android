package com.example.alf.data.model.match

import com.example.alf.data.model.EventModel

data class MainModel (
    var match: MatchModel,
    //var referees:
    val hostEvents: List<EventModel>,
    val guestEvents: List<EventModel>,
)
