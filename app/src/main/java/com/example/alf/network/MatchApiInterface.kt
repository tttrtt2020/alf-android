package com.example.alf.network

import com.example.alf.data.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchApiInterface {

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPageModel>

    @GET("matches?sort=dateTime,desc")
    suspend fun fetchMatchesPage(@Query("page") page: Int): MatchesPageModel

    @GET("matches/{id}")
    fun fetchMatchById(@Path("id") id: Int): Call<MatchModel>

}