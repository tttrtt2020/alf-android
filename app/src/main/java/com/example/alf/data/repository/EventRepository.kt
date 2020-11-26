package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.event.Event
import com.example.alf.network.ApiClient
import com.example.alf.network.EventApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository {

    private var eventApiInterface: EventApiInterface? = null

    init {
        eventApiInterface = ApiClient.getApiClient().create(EventApiInterface::class.java)
    }

    fun fetchAllEvents(): LiveData<List<Event>> {
        val data = MutableLiveData<List<Event>>()

        eventApiInterface?.fetchAllEvents()?.enqueue(object : Callback<List<Event>> {

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                    call: Call<List<Event>>,
                    response: Response<List<Event>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }
        })

        return data
    }

    /*fun createEvent(eventModel: EventModel):LiveData<EventModel>{
        val data = MutableLiveData<EventModel>()

        apiInterface?.createEvent(eventModel)?.enqueue(object : Callback<EventModel>{
            override fun onFailure(call: Call<EventModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<EventModel>, response: Response<EventModel>) {
                val res = response.body()
                if (response.code() == 201 && res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })

        return data

    }

    fun deleteEvent(id:Int):LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()

        apiInterface?.deleteEvent(id)?.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                data.value = response.code() == 200
            }
        })

        return data

    }*/

}