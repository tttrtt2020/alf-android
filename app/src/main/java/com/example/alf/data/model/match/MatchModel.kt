package com.example.alf.data.model.match

import com.example.alf.data.model.Team
import java.util.*

data class MatchModel(
        var id: Int,
        var dateTime: Date?,
        var stadium: StadiumModel?,
        var hostTeam: Team,
        var guestTeam: Team,
        var status: String,
        var result: ResultModel
)