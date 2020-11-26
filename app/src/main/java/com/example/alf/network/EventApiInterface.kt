package com.example.alf.network

import com.example.alf.data.model.event.Event
import retrofit2.Call
import retrofit2.http.GET

interface EventApiInterface {

    @GET("events")
    fun fetchAllEvents(): Call<List<Event>>

}