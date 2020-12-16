package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.RefereesPage
import com.example.alf.data.model.Referee
import com.example.alf.network.ApiClient
import com.example.alf.network.RefereeApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefereeApiService {

    private var refereeApiInterface: RefereeApiInterface = ApiClient.getApiClient().create(RefereeApiInterface::class.java)

    fun getRefereeById(refereeLiveData: MutableLiveData<Referee>, id: Int): LiveData<Referee> {

        refereeApiInterface.fetchRefereeById(id).enqueue(object : Callback<Referee> {

            override fun onFailure(call: Call<Referee>, t: Throwable) {
                refereeLiveData.value = null
            }

            override fun onResponse(
                    call: Call<Referee>,
                    response: Response<Referee>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    refereeLiveData.value = res
                } else {
                    refereeLiveData.value = null
                }
            }
        })

        return refereeLiveData
    }

    suspend fun fetchRefereesPage(query: String, sort: String, nextPageNumber: Int): RefereesPage {
        return refereeApiInterface.fetchRefereesPage(query, sort, nextPageNumber)
    }

    fun createReferee(refereeLiveData: MutableLiveData<Referee>, referee: Referee): LiveData<Referee>{

        refereeApiInterface.createReferee(referee).enqueue(object : Callback<Referee>{
            override fun onFailure(call: Call<Referee>, t: Throwable) {
                refereeLiveData.value = null
            }

            override fun onResponse(call: Call<Referee>, response: Response<Referee>) {
                val res = response.body()
                if (response.code() == 201 && res != null) {
                    refereeLiveData.value = res
                } else {
                    refereeLiveData.value = null
                }
            }
        })

        return refereeLiveData
    }

    fun updateReferee(resultLiveData: MutableLiveData<Boolean?>, referee: Referee): LiveData<Boolean?>{

        refereeApiInterface.updateReferee(referee.id, referee).enqueue(object : Callback<Referee>{
            override fun onFailure(call: Call<Referee>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Referee>, response: Response<Referee>) {
                resultLiveData.value = (response.code() == 200 || response.code() == 204)
            }
        })

        return resultLiveData
    }

    fun deleteReferee(resultLiveData: MutableLiveData<Boolean?>, referee: Referee): LiveData<Boolean?> {

        refereeApiInterface.deleteReferee(referee.id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                resultLiveData.value = (response.code() == 200 || response.code() == 204)
            }
        })

        return resultLiveData
    }

}