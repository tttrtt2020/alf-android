package com.example.alf.network

import com.example.alf.data.model.*
import com.example.alf.data.model.match.MatchPerson
import retrofit2.Call
import retrofit2.http.*

interface MatchApiInterface {

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPage>

    @GET("matches?sort=dateTime,desc")
    suspend fun fetchMatchesPage(@Query("query") query: String, @Query("page") page: Int): MatchesPage

    @GET("matches/{id}")
    fun fetchMatchById(@Path("id") id: Int): Call<Match>

    @GET("matches/{matchId}/referees")
    fun fetchMatchReferees(@Path("matchId") matchId: Int): Call<List<Referee>>

    @POST("matches/{matchId}/referees")
    fun addMatchReferee(@Path("matchId") matchId: Int, @Body referee: Referee): Call<Referee>

    @DELETE("matches/{matchId}/referees/{refereeId}")
    fun deleteMatchReferee(@Path("matchId") matchId: Int, @Path("refereeId") refereeId: Int): Call<Unit>

    @GET("matches/{matchId}/{teamId}/matchTeam")
    fun fetchMatchTeam(@Path("matchId") matchId: Int, @Path("teamId") teamId: Int): Call<MatchTeam>

    @GET("matches/{matchId}/{teamId}/squad")
    fun fetchMatchTeamSquad(@Path("matchId") matchId: Int, @Path("teamId") teamId: Int): Call<List<MatchPerson>>

    @GET("matches/{matchId}/{teamId}/allowablePlayers")
    suspend fun fetchMatchTeamAllowablePlayers(
            @Path("matchId") matchId: Int, @Path("teamId") teamId: Int,
            @Query("query") query: String,
            @Query("page") page: Int, @Query("sort") field: String
    ): PlayersPage

    @POST("matches/{matchId}/{teamId}/players")
    fun addMatchPlayer(@Path("matchId") matchId: Int, @Path("teamId") teamId: Int, @Body player: Player): Call<Player>

}