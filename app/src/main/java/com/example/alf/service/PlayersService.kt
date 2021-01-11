package com.example.alf.service

import com.example.alf.data.model.PlayersPage
import com.example.alf.data.repository.PlayerApiService

class PlayersService {

    suspend fun searchPlayersPageForAppearance(
            matchId: Int, teamId: Int,
            query: String,
            sort: String,
            nextPageNumber: Int): PlayersPage {
        return PlayerApiService().fetchMatchTeamAllowablePlayersPage(
                matchId, teamId,
                query, sort, nextPageNumber
        )
    }

    suspend fun searchPlayersPageForEvent(
            matchId: Int, teamId: Int, eventTypeId: Int, minute: Int,
            query: String,
            sort: String,
            nextPageNumber: Int): PlayersPage {
        return PlayerApiService().fetchMatchEventAllowablePlayersPage(
                matchId, teamId, eventTypeId, minute,
                query, sort, nextPageNumber
        )
    }

}
