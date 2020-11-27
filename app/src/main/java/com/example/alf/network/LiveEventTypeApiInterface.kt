package com.example.alf.network

import com.example.alf.data.model.event.LiveEventType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface LiveEventTypeApiInterface {

    @GET("eventTypes")
    fun fetchLiveEventTypes(): Call<List<LiveEventType>>

    @GET("eventTypes/{id}")
    fun fetchLiveEventTypeById(@Path("id") id: Int): Call<LiveEventType>

}