package com.example.alf.network

import com.example.alf.data.model.match.Status
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface StatusApiInterface {

    @GET("matches/{matchId}/allowableStatuses")
    fun fetchAllowableStatuses(
            @Path("matchId") matchId: Int
    ): Call<List<Status>>

    @PUT("matches/{matchId}/status")
    fun setStatus(
            @Path("matchId") matchId: Int,
            @Body status: Status
    ): Call<Status>

}