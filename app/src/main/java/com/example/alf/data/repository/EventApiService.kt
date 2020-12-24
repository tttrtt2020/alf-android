package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.event.Event
import com.example.alf.network.ApiClient
import com.example.alf.network.EventApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventApiService {

    private var eventApiInterface: EventApiInterface = ApiClient.getApiClient().create(
            EventApiInterface::class.java
    )

    fun updateEvent(resultLiveData: MutableLiveData<Boolean?>, event: Event): LiveData<Boolean?>{

        eventApiInterface.updateEvent(event.id, event).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                resultLiveData.value = (response.code() >= 200 || response.code() <= 299)
            }
        })

        return resultLiveData
    }

    fun updateMatchEvent(resultLiveData: MutableLiveData<Boolean?>, matchId: Int, event: Event): LiveData<Boolean?>{

        eventApiInterface.updateMatchEvent(matchId, event.id, event).enqueue(object : Callback<Event>{
            override fun onFailure(call: Call<Event>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                resultLiveData.value = (response.code() >= 200 || response.code() <= 299)
            }
        })

        return resultLiveData
    }

    fun deleteEvent(resultLiveData: MutableLiveData<Boolean?>, event: Event): LiveData<Boolean?> {

        eventApiInterface.deleteEvent(event.id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                resultLiveData.value = (response.code() == 200 || response.code() == 204)
            }
        })

        return resultLiveData
    }

    fun fetchMatchEvents(eventsLiveData: MutableLiveData<List<Event>?>, matchId: Int): LiveData<List<Event>?> {

        eventApiInterface.fetchMatchEvents(matchId).enqueue(object : Callback<List<Event>?> {

            override fun onFailure(call: Call<List<Event>?>, t: Throwable) {
                eventsLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<Event>?>,
                    response: Response<List<Event>?>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    eventsLiveData.value = res
                } else {
                    eventsLiveData.value = null
                }
            }
        })

        return eventsLiveData
    }

}