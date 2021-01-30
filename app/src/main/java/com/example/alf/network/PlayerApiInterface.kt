package com.example.alf.network

import com.example.alf.data.model.PlayersPage
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlayerApiInterface {

    @GET("matches/{matchId}/{teamId}/appearanceAllowablePlayers")
    suspend fun fetchMatchTeamAllowablePlayers(
        @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
        @Query("query") query: String,
        @Query("sort") field: String,
        @Query("page") page: Int
    ): PlayersPage

    @GET("matches/{matchId}/{teamId}/{eventTypeId}/{minute}/eventAllowablePlayers")
    suspend fun fetchMatchEventAllowablePlayers(
            @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
            @Path("eventTypeId") eventTypeId: Int, @Path("minute") minute: Int,
            @Query("query") query: String,
            @Query("sort") field: String,
            @Query("page") page: Int
    ): PlayersPage

    @GET("matches/{matchId}/{teamId}/{minute}/substitutionAllowableOutPlayers")
    suspend fun fetchMatchSubstitutionOutAllowablePlayers(
            @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
            @Path("minute") minute: Int,
            @Query("query") query: String,
            @Query("sort") field: String,
            @Query("page") page: Int
    ): PlayersPage

    @GET("matches/{matchId}/{teamId}/{minute}/substitutionAllowableInPlayers")
    suspend fun fetchMatchSubstitutionInAllowablePlayers(
            @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
            @Path("minute") minute: Int,
            @Query("query") query: String,
            @Query("sort") field: String,
            @Query("page") page: Int
    ): PlayersPage

}