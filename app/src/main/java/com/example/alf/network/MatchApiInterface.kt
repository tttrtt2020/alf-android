package com.example.alf.network

import com.example.alf.data.model.Match
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.Team
import com.example.alf.data.model.match.YoutubeUrl
import retrofit2.Call
import retrofit2.http.*

interface MatchApiInterface {

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPage>

    @GET("matches/played")
    //@GET("matches")
    suspend fun fetchMatchesPage(
            @Query("query") query: String,
            @Query("sort") sort: String,
            @Query("page") page: Int,
    ): MatchesPage

    @GET("matches/{id}")
    fun fetchMatchById(@Path("id") id: Int): Call<Match>

    @GET("matches/{matchId}/{teamId}/matchTeam")
    fun fetchMatchTeam(
            @Path("matchId") matchId: Int,
            @Path("teamId") teamId: Int
    ): Call<MatchTeam>

    @GET("matches/{matchId}/teams")
    fun fetchTeams(@Path("matchId") matchId: Int): Call<List<Team>>

    @PUT("matches/{matchId}/youtubeId")
    fun updateYoutubeId(
        @Path("matchId") matchId: Int,
        @Body youtubeId: YoutubeUrl
    ): Call<YoutubeUrl>

}