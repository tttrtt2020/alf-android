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
                resultLiveData.value = (response.code() >= 200 || response.code() <= 299)
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

    fun fetchMatchReferees(
            matchRefereesLiveData: MutableLiveData<List<Referee>?>,
            matchId: Int
    ): LiveData<List<Referee>?> {

        refereeApiInterface.fetchMatchReferees(matchId).enqueue(object : Callback<List<Referee>> {

            override fun onFailure(call: Call<List<Referee>>, t: Throwable) {
                matchRefereesLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<Referee>>,
                    response: Response<List<Referee>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    matchRefereesLiveData.value = res
                } else {
                    matchRefereesLiveData.value = null
                }
            }
        })

        return matchRefereesLiveData
    }

    suspend fun fetchAllowableMatchRefereesPage(matchId: Int, query: String, sort: String, nextPageNumber: Int): RefereesPage {
        return refereeApiInterface.fetchAllowableMatchRefereesPage(matchId, query, sort, nextPageNumber)
    }

    fun addMatchReferee(
            addRefereeToMatchLiveData: MutableLiveData<Boolean?>,
            matchId: Int,
            referee: Referee
    ): LiveData<Boolean?> {

        refereeApiInterface.addMatchReferee(matchId, referee).enqueue(object : Callback<Referee> {
            override fun onFailure(call: Call<Referee>, t: Throwable) {
                addRefereeToMatchLiveData.value = false
            }

            override fun onResponse(call: Call<Referee>, response: Response<Referee>) {
                addRefereeToMatchLiveData.value = response.code() == 201
            }
        })

        return addRefereeToMatchLiveData
    }

    fun deleteMatchReferee(
            resultLiveData: MutableLiveData<Boolean?>,
            matchId: Int,
            referee: Referee
    ): LiveData<Boolean?> {

        refereeApiInterface.deleteMatchReferee(matchId, referee.id).enqueue(object : Callback<Unit> {
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