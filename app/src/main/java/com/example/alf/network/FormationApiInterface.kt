package com.example.alf.network

import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.FormationsPageModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FormationApiInterface {

    @GET("formations")
    fun fetchFormationsPage(): Call<FormationsPageModel>

    @GET("formations/{id}")
    fun fetchFormationById(@Path("id") id: Int): Call<Formation>

}