package com.example.alf.network

import com.example.alf.data.model.*
import retrofit2.Call
import retrofit2.http.*

interface PersonApiInterface {

    @GET("persons")
    fun fetchPersonsPage(): Call<PersonsPage>

    @GET("persons")
    suspend fun fetchPersonsPage(@Query("lastName") lastName: String, @Query("page") page: Int, @Query("sort") field: String): PersonsPage

    /*@GET("persons")
    fun fetchPersonsPageByQuery(@Query("lastName") lastName: String): Call<PersonsPageModel>*/

    @GET("persons")
    suspend fun fetchPersonsPageByQuery(@Query("lastName") lastName: String): PersonsPage

    @GET("persons/{id}")
    fun fetchPersonById(@Path("id") id: Int): Call<Person>

    @POST("persons")
    fun createPerson(@Body person: Person): Call<Person>

    @PUT("persons/{id}")
    fun updatePerson(@Path("id") id: Int, @Body person: Person): Call<Person>

    @DELETE("persons/{id}")
    fun deletePerson(@Path("id") id: Int): Call<Unit>

}