package com.example.alf.service

import com.example.alf.data.model.PlayersPage
import com.example.alf.data.repository.MatchApiService

class PlayersService {

    suspend fun searchPlayersPage(
            matchId: Int, teamId: Int,
            query: String,
            sort: String,
            nextPageNumber: Int): PlayersPage {
        return MatchApiService().fetchMatchTeamAllowablePlayersPage(matchId, teamId, query, sort, nextPageNumber)
    }

}
