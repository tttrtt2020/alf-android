package com.example.alf.network

import com.example.alf.data.model.event.Event
import retrofit2.Call
import retrofit2.http.*

interface EventApiInterface {

    @GET("events")
    fun fetchAllEvents(): Call<List<Event>>

    @PUT("events/{id}")
    fun updateEvent(@Path("id") id: Int, @Body event: Event): Call<Event>

    @DELETE("events/{id}")
    fun deleteEvent(@Path("id") id: Int): Call<Unit>

    @GET("matches/{matchId}/events")
    fun fetchMatchEvents(@Path("matchId") matchId: Int): Call<List<Event>>

}