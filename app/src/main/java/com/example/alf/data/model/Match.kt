package com.example.alf.data.model

import java.util.*

data class Match(
        var id: Int,
        var dateTime: Date?,
        var hostMatchTeam: MatchTeam,
        var guestMatchTeam: MatchTeam,
        var status: String,
        var resultHostGoals: Int?,
        var resultGuestGoals: Int?
)