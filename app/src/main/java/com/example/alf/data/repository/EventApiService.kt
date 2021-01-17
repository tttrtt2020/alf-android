package com.example.alf.data.repository

import com.example.alf.data.model.event.Event
import com.example.alf.network.ApiClient
import com.example.alf.network.EventApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventApiService {

    private var eventApiInterface: EventApiInterface = ApiClient.getApiClient().create(EventApiInterface::class.java)

    fun updateEvent(
            matchId: Int,
            event: Event,
            successCallback: (event: Event) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        eventApiInterface.updateMatchEvent(matchId, event.id, event).enqueue(object : Callback<Event>{

            override fun onFailure(call: Call<Event>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun createEvent(
            matchId: Int,
            event: Event,
            successCallback: (event: Event) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        eventApiInterface.createMatchEvent(matchId, event).enqueue(object : Callback<Event> {

            override fun onFailure(call: Call<Event>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                if (response.code() == 201) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun deleteEvent(
            event: Event,
            successCallback: () -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        eventApiInterface.deleteEvent(event.id).enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() == 200 || response.code() == 204) {
                    successCallback()
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun fetchMatchEvents(
            matchId: Int,
            successCallback: (events: List<Event>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        eventApiInterface.fetchMatchEvents(matchId).enqueue(object : Callback<List<Event>> {

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    successCallback(res)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}