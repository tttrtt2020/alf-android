package com.example.alf.data.model

import java.io.Serializable
import java.util.*

data class Match(
        var id: Int,
        var dateTime: Date?,
        var stadium: Stadium?,
        var hostTeam: Team,
        var guestTeam: Team,
        var status: String,
        var resultHostGoals: Int,
        var resultGuestGoals: Int
) : Serializable {
    companion object {
        const val STATUS_FINISHED: String = "FINISHED"
    }
}