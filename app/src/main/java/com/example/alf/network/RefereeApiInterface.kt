package com.example.alf.network

import com.example.alf.data.model.*
import com.example.alf.data.model.Referee
import retrofit2.Call
import retrofit2.http.*

interface RefereeApiInterface {

    @GET("referees")
    fun fetchRefereesPage(): Call<RefereesPage>

    @GET("referees")
    suspend fun fetchRefereesPage(@Query("lastName") lastName: String, @Query("page") page: Int, @Query("sort") field: String): RefereesPage

    /*@GET("referees")
    fun fetchRefereesPageByQuery(@Query("lastName") lastName: String): Call<RefereesPage>*/

    @GET("referees")
    suspend fun fetchRefereesPageByQuery(@Query("lastName") lastName: String): RefereesPage

    @GET("referees/{id}")
    fun fetchRefereeById(@Path("id") id: Int): Call<Referee>

    @POST("referees")
    fun createReferee(@Body referee: Referee): Call<Referee>

    @PUT("referees/{id}")
    fun updateReferee(@Path("id") id: Int, @Body referee: Referee): Call<Referee>

    @DELETE("referees/{id}")
    fun deleteReferee(@Path("id") id: Int): Call<Unit>

}