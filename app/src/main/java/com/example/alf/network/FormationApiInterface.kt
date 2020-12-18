package com.example.alf.network

import com.example.alf.data.model.match.Formation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FormationApiInterface {

    @GET("matches/{matchId}/{teamId}/allowableFormations")
    fun fetchAllowableFormations(
            @Path("matchId") matchId: Int,
            @Path("teamId") teamId: Int
    ): Call<List<Formation>>

    @GET("formations/{id}")
    fun fetchFormationById(@Path("id") id: Int): Call<Formation>

}