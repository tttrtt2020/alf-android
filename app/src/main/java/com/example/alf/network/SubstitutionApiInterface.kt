package com.example.alf.network

import com.example.alf.data.model.Substitution
import retrofit2.Call
import retrofit2.http.*

interface SubstitutionApiInterface {

    @GET("matches/{matchId}/substitutions")
    fun fetchMatchSubstitutions(
            @Path("matchId") matchId: Int
    ): Call<List<Substitution>>

    @POST("matches/{matchId}/substitutions")
    fun createMatchSubstitution(
            @Path("matchId") matchId: Int,
            @Body substitution: Substitution
    ): Call<Substitution>

    @DELETE("matches/{matchId}/substitutions")
    fun deleteMatchSubstitution(
            @Path("matchId") matchId: Int,
            @Body substitution: Substitution
    ): Call<Unit>

}