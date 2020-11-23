package com.example.alf.network

import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.match.MatchInfo
import com.example.alf.data.model.match.SquadsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchApiInterface {

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPage>

    @GET("matches?sort=dateTime,desc")
    suspend fun fetchMatchesPage(@Query("page") page: Int): MatchesPage

    @GET("getMatchInfo/{id}")
    fun fetchMatchInfoById(@Path("id") id: Int): Call<MatchInfo>

    @GET("getMatchSquadsInfo/{id}")
    fun fetchMatchSquadsInfoById(@Path("id") id: Int): Call<SquadsModel>

}