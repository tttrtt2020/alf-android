package com.example.alf.network

import com.example.alf.AlfApplication
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    companion object {
        private var retrofit: Retrofit? = null
        fun getApiClient(): Retrofit {
            val gson = GsonBuilder()
                .setDateFormat(AlfApplication.getProperty("api.dateFormat"))
                .setLenient()
                .create()
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(AlfApplication.getProperty("api.readTimeout").toLong(), TimeUnit.SECONDS)
                .connectTimeout(AlfApplication.getProperty("api.connectTimeout").toLong(), TimeUnit.SECONDS)
                .build()
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(AlfApplication.getProperty("api.baseUrl"))
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit!!
        }
    }
}