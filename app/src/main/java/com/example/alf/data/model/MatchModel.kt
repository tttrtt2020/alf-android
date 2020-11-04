package com.example.alf.data.model

import java.util.*

data class MatchModel(
    var id: Int,
    var dateTime: Date?,
    var hostMatchTeam: MatchTeamModel,
    var guestMatchTeam: MatchTeamModel,
    var status: String,
    var resultHostGoals: Int?,
    var resultGuestGoals: Int?
)