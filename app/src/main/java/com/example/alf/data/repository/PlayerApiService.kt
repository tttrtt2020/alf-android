package com.example.alf.data.repository

import com.example.alf.data.model.PlayersPage
import com.example.alf.data.model.Substitution
import com.example.alf.network.ApiClient
import com.example.alf.network.PlayerApiInterface

class PlayerApiService {

    private var playerApiInterface = ApiClient.getApiClient().create(PlayerApiInterface::class.java)

    suspend fun fetchMatchTeamAllowablePlayersPage(
        matchId: Int, teamId: Int,
        query: String, sort: String, nextPageNumber: Int
    ): PlayersPage {
        return playerApiInterface.fetchMatchTeamAllowablePlayers(
                matchId, teamId,
                query, sort, nextPageNumber
        )
    }

    suspend fun fetchMatchEventAllowablePlayersPage(
        matchId: Int, teamId: Int, eventTypeId: Int, minute: Int,
        query: String, sort: String, nextPageNumber: Int
    ): PlayersPage {
        return playerApiInterface.fetchMatchEventAllowablePlayers(
                matchId, teamId, eventTypeId, minute,
                query, sort, nextPageNumber
        )
    }

    suspend fun fetchMatchSubstitutionAllowablePlayersPage(
            matchId: Int, teamId: Int, minute: Int, playerType: Substitution.PlayerType,
            query: String, sort: String, nextPageNumber: Int
    ): PlayersPage {
        return when (playerType) {
            Substitution.PlayerType.OUT -> playerApiInterface.fetchMatchSubstitutionOutAllowablePlayers(
                    matchId, teamId, minute,
                    query, sort, nextPageNumber
            )
            Substitution.PlayerType.IN -> playerApiInterface.fetchMatchSubstitutionInAllowablePlayers(
                    matchId, teamId, minute,
                    query, sort, nextPageNumber
            )
        }
    }

}