package com.example.alf.network

import com.example.alf.data.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("persons")
    fun fetchPersonsPage(): Call<PersonsPageModel>

    @GET("persons")
    suspend fun fetchPersonsPage(@Query("lastName") lastName: String, @Query("page") page: Int): PersonsPageModel

    /*@GET("persons")
    fun fetchPersonsPageByQuery(@Query("lastName") lastName: String): Call<PersonsPageModel>*/

    @GET("persons")
    suspend fun fetchPersonsPageByQuery(@Query("lastName") lastName: String): PersonsPageModel

    @GET("persons/{id}")
    fun fetchPersonById(@Path("id") id: Int): Call<PersonModel>

    @GET("matches?sort=dateTime,desc")
    fun fetchMatchesPage(): Call<MatchesPageModel>

    @GET("matches?sort=dateTime,desc")
    suspend fun fetchMatchesPage(@Query("page") page: Int): MatchesPageModel

    @GET("matches/{id}")
    fun fetchMatchById(@Path("id") id: Int): Call<MatchModel>

    @GET("events")
    fun fetchAllEvents(): Call<List<EventModel>>
}