package com.example.alf.network

import com.example.alf.data.model.*
import com.example.alf.data.model.match.MatchPerson
import retrofit2.Call
import retrofit2.http.*

interface MatchApiInterface {

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPage>

    @GET("matches/played?sort=dateTime,desc")
    suspend fun fetchMatchesPage(@Query("page") page: Int): MatchesPage

    @GET("matches/{id}")
    fun fetchMatchById(@Path("id") id: Int): Call<Match>

    @GET("matches/{matchId}/referees")
    fun fetchMatchReferees(@Path("matchId") matchId: Int): Call<List<Referee>>

    @POST("matches/{matchId}/referees")
    fun addMatchReferee(@Path("matchId") matchId: Int, @Body referee: Referee): Call<Referee>

    @GET("matches/{matchId}/{teamId}/squad")
    fun fetchMatchTeamSquad(@Path("matchId") matchId: Int, @Path("teamId") teamId: Int): Call<List<MatchPerson>>

}