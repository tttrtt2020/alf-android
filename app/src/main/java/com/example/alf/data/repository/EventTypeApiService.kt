package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.event.EventType
import com.example.alf.network.ApiClient
import com.example.alf.network.EventTypeApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventTypeApiService {

    private var formationApiInterface: EventTypeApiInterface = ApiClient.getApiClient().create(EventTypeApiInterface::class.java)

    fun fetchMatchEventTypes(
            eventTypesLiveData: MutableLiveData<List<EventType>?>,
            matchId: Int
    ): LiveData<List<EventType>?> {

        formationApiInterface.fetchMatchEventTypes(matchId).enqueue(object : Callback<List<EventType>> {

            override fun onFailure(call: Call<List<EventType>>, t: Throwable) {
                eventTypesLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<EventType>>,
                    response: Response<List<EventType>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    eventTypesLiveData.value = res
                } else {
                    eventTypesLiveData.value = null
                }
            }
        })

        return eventTypesLiveData
    }

}