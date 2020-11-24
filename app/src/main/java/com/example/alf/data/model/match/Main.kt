package com.example.alf.data.model.match

import com.example.alf.data.model.Event

data class Main (
        var match: Match,
    //var referees:
        val hostEvents: List<Event>,
        val guestEvents: List<Event>,
)
