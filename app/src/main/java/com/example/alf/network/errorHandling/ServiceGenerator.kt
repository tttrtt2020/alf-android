package com.example.alf.network.errorHandling

import com.example.alf.AlfApplication
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceGenerator {
    private val BASE_URL: String = AlfApplication.getProperty("api.baseUrl")
    private val builder = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
    var retrofit: Retrofit = builder.build()
    private val httpClient = OkHttpClient.Builder()
    fun <S> createService(
        serviceClass: Class<S>?
    ): S {
        return retrofit.create(serviceClass)
    }
}