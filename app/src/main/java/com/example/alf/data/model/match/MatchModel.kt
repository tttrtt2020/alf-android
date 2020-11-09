package com.example.alf.data.model.match

import com.example.alf.data.model.TeamModel
import java.util.*

data class MatchModel(
    var id: Int,
    var dateTime: Date?,
    var hostTeam: TeamModel,
    var guestTeam: TeamModel,
    var status: String,
    var result: ResultModel
)