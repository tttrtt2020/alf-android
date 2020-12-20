package com.example.alf.network

import com.example.alf.data.model.event.EventType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface EventTypeApiInterface {

    @GET("eventTypes")
    fun fetchEventTypes(): Call<List<EventType>>

    @GET("eventTypes/{id}")
    fun fetchEventTypeById(@Path("id") id: Int): Call<EventType>

}