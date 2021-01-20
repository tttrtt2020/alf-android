package com.example.alf.network

import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Appearance
import retrofit2.Call
import retrofit2.http.*

interface AppearanceApiInterface {

    @GET("matches/{matchId}/{teamId}/squad")
    fun fetchMatchTeamAppearances(
        @Path("matchId") matchId: Int,
        @Path("teamId") teamId: Int
    ): Call<List<Appearance>>

    @POST("matches/{matchId}/{teamId}/players")
    fun addAppearance(
        @Path("matchId") matchId: Int,
        @Path("teamId") teamId: Int,
        @Query("fieldPositionId") fieldPositionId: Int?,
        @Body player: Player
    ): Call<Player>

    @DELETE("matches/{matchId}/{playerId}")
    fun deleteAppearance(
        @Path("matchId") matchId: Int,
        @Path("playerId") playerId: Int
    ): Call<Unit>

}