package com.example.alf.network

import com.example.alf.data.model.match.FieldPosition
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FieldPositionApiInterface {

    @GET("matches/{matchId}/{teamId}/freeFieldPositions")
    fun fetchFreeFieldPositions(
            @Path("matchId") matchId: Int,
            @Path("teamId") teamId: Int
    ): Call<List<FieldPosition>>

}