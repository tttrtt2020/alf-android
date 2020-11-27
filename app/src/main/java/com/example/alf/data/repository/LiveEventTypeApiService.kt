package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.event.LiveEventType
import com.example.alf.network.ApiClient
import com.example.alf.network.LiveEventTypeApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LiveEventTypeApiService {

    private var formationApiInterface: LiveEventTypeApiInterface = ApiClient.getApiClient().create(LiveEventTypeApiInterface::class.java)

    fun fetchLiveEventTypes(liveEventTypesLiveData: MutableLiveData<List<LiveEventType>>): LiveData<List<LiveEventType>> {

        formationApiInterface.fetchLiveEventTypes().enqueue(object : Callback<List<LiveEventType>> {

            override fun onFailure(call: Call<List<LiveEventType>>, t: Throwable) {
                liveEventTypesLiveData.value = null
            }

            override fun onResponse(
                call: Call<List<LiveEventType>>,
                response: Response<List<LiveEventType>>
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

    fun fetchLiveEventTypeById(liveEventTypeLiveData: MutableLiveData<LiveEventType>, id: Int): LiveData<LiveEventType> {

        formationApiInterface.fetchLiveEventTypeById(id).enqueue(object : Callback<LiveEventType> {

            override fun onFailure(call: Call<LiveEventType>, t: Throwable) {
                liveEventTypeLiveData.value = null
            }

            override fun onResponse(
                call: Call<LiveEventType>,
                response: Response<LiveEventType>
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