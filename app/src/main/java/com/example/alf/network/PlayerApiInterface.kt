package com.example.alf.network

import com.example.alf.data.model.Player
import com.example.alf.data.model.PlayersPage
import com.example.alf.data.model.match.MatchPlayer
import retrofit2.Call
import retrofit2.http.*

interface PlayerApiInterface {

    @GET("matches/{matchId}/{teamId}/squad")
    fun fetchMatchTeamSquad(
        @Path("matchId") matchId: Int,
        @Path("teamId") teamId: Int
    ): Call<List<MatchPlayer>>

    @GET("matches/{matchId}/{teamId}/allowablePlayers")
    suspend fun fetchMatchTeamAllowablePlayers(
        @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
        @Query("query") query: String,
        @Query("sort") field: String,
        @Query("page") page: Int
    ): PlayersPage

    @POST("matches/{matchId}/{teamId}/players")
    fun addMatchPlayer(
        @Path("matchId") matchId: Int,
        @Path("teamId") teamId: Int,
        @Query("fieldPositionId") fieldPositionId: Int?,
        @Body player: Player
    ): Call<Player>

    @DELETE("matches/{matchId}/{playerId}")
    fun deleteMatchPlayer(
        @Path("matchId") matchId: Int,
        @Path("playerId") playerId: Int
    ): Call<Unit>

}