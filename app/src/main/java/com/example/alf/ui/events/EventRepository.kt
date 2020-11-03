package com.example.alf.ui.events

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.network.ApiClient
import com.example.alf.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventRepository {

    private var apiInterface: ApiInterface? = null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }

    fun fetchAllEvents(): LiveData<List<EventModel>> {
        val data = MutableLiveData<List<EventModel>>()

        apiInterface?.fetchAllEvents()?.enqueue(object : Callback<List<EventModel>> {

            override fun onFailure(call: Call<List<EventModel>>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<List<EventModel>>,
                response: Response<List<EventModel>>
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