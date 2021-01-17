package com.example.alf.data.repository

import com.example.alf.data.model.event.EventType
import com.example.alf.network.ApiClient
import com.example.alf.network.EventTypeApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventTypeApiService {

    private var formationApiInterface: EventTypeApiInterface = ApiClient.getApiClient().create(EventTypeApiInterface::class.java)

    fun fetchMatchEventTypes(
            matchId: Int,
            successCallback: (eventTypes: List<EventType>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        formationApiInterface.fetchMatchEventTypes(matchId).enqueue(object : Callback<List<EventType>> {

            override fun onFailure(call: Call<List<EventType>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<EventType>>,
                    response: Response<List<EventType>>
            ) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}