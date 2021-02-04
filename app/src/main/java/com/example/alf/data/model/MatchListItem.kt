package com.example.alf.data.model

import java.io.Serializable
import java.util.*

data class MatchListItem(
        var id: Int,
        var dateTime: Date?,
        var stadium: Stadium?,
        var hostTeam: Team,
        var guestTeam: Team,
        var status: String,
        var resultHostGoals: Int,
        var resultGuestGoals: Int,
) : Serializable {
    companion object {
        // TODO: 2/4/21 use same statuses as in Match class
        const val STATUS_FINISHED: String = "FINISHED"
    }
}