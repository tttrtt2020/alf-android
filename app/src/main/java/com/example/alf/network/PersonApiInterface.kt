package com.example.alf.network

import com.example.alf.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface PersonApiInterface {

    @GET("persons")
    fun fetchPersonsPage(): Call<PersonsPage>

    @GET("persons")
    suspend fun fetchPersonsPage(
            @Query("query") query: String,
            @Query("sort") sort: String,
            @Query("page") page: Int,
    ): PersonsPage

    @GET("persons/{id}")
    fun fetchPersonById(@Path("id") id: Int): Call<Person>

    @POST("persons")
    fun createPerson(@Body person: Person): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Unit>

}