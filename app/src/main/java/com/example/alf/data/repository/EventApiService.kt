package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.event.Event
import com.example.alf.network.ApiClient
import com.example.alf.network.EventApiInterface
import com.example.alf.network.Resource
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import com.example.alf.ui.common.ViewEvent
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

    fun createEvent(
            addEventToMatchLiveData: MutableLiveData<ViewEvent<Boolean>>,
            matchId: Int,
            event: Event
    ): LiveData<ViewEvent<Boolean>> {

        eventApiInterface.createMatchEvent(matchId, event).enqueue(object :
                Callback<Event> {
            override fun onFailure(call: Call<Event>, t: Throwable) {
                addEventToMatchLiveData.value = ViewEvent(false)
            }

            override fun onResponse(call: Call<Event>, response: Response<Event>) {
                addEventToMatchLiveData.value = ViewEvent(response.code() == 201)
            }
        })

        return addEventToMatchLiveData
    }

    fun deleteEvent(
            event: Event,
            resultCallback: (result: Boolean) -> Unit
    ) {
        eventApiInterface.deleteEvent(event.id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                resultCallback(false)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                val result = response.code() == 200 || response.code() == 204
                resultCallback(result)
            }
        })
    }

    fun fetchMatchEvents(
            eventsLiveData: MutableLiveData<Resource<List<Event>>>,
            matchId: Int
    ) {
        eventApiInterface.fetchMatchEvents(matchId).enqueue(object : Callback<List<Event>> {

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                eventsLiveData.value = Resource.Error(t.localizedMessage!!, null)
            }

            override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    eventsLiveData.value = Resource.Success(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    eventsLiveData.value = Resource.Error(apiError.message, null)
                }
            }
        })
    }

}