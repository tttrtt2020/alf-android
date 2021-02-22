package com.example.alf.data.model

import com.example.alf.data.model.match.Status
import java.io.Serializable
import java.util.*

data class Match(
        var id: Int,
        var format: Format,
        var dateTime: Date?,
        var stadium: Stadium?,
        var hostTeam: Team,
        var guestTeam: Team,
        var status: Status,
        var scoreHost: Int,
        var scoreGuest: Int,
        var youtubeId: String?,
        var competitionName: String?
) : Serializable {
    companion object {
        const val STATUS_FINISHED: String = "FINISHED"
    }
}