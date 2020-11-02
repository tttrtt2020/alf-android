package com.example.alf.network

import com.example.alf.ui.events.EventModel
import com.example.alf.ui.persons.PersonsPageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("persons")
    fun fetchPersonsPage(@Query("page") page: Int): Call<PersonsPageModel>

    @GET("persons")
    fun fetchPersonsPageByQuery(@Query("lastName") lastName: String): Call<PersonsPageModel>

    @GET("events")
    fun fetchAllEvents(): Call<List<EventModel>>
}