package com.example.alf.network

import com.example.alf.ui.persons.PersonsPageModel
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {
    //@GET("persons")
    @GET("persons?page=150")
    //@GET("persons?lastName=Белаш")
    fun fetchAllPersons(): Call<PersonsPageModel>
}