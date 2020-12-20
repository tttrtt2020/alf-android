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

    fun fetchEventTypes(liveEventTypesLiveData: MutableLiveData<List<EventType>>): LiveData<List<EventType>> {

        formationApiInterface.fetchEventTypes().enqueue(object : Callback<List<EventType>> {

            override fun onFailure(call: Call<List<EventType>>, t: Throwable) {
                liveEventTypesLiveData.value = null
            }

            override fun onResponse(
                call: Call<List<EventType>>,
                response: Response<List<EventType>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    liveEventTypesLiveData.value = res
                } else {
                    liveEventTypesLiveData.value = null
                }
            }
        })

        return liveEventTypesLiveData
    }

    fun fetchEventTypeById(liveEventTypeLiveData: MutableLiveData<EventType>, id: Int): LiveData<EventType> {

        formationApiInterface.fetchEventTypeById(id).enqueue(object : Callback<EventType> {

            override fun onFailure(call: Call<EventType>, t: Throwable) {
                liveEventTypeLiveData.value = null
            }

            override fun onResponse(
                call: Call<EventType>,
                response: Response<EventType>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    liveEventTypeLiveData.value = res
                } else {
                    liveEventTypeLiveData.value = null
                }
            }
        })

        return liveEventTypeLiveData
    }
}