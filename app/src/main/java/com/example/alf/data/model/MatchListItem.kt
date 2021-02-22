package com.example.alf.data.model

import com.example.alf.data.model.match.Status
import java.io.Serializable
import java.util.*

data class MatchListItem(
        var id: Int,
        var dateTime: Date?,
        var stadium: Stadium?,
        var hostTeam: Team,
        var guestTeam: Team,
        var status: Status,
        var scoreHost: Int,
        var scoreGuest: Int,
) : Serializable {
    companion object {
        // TODO: 2/4/21 use same statuses as in Match class
        const val STATUS_FINISHED: String = "FINISHED"
    }
}