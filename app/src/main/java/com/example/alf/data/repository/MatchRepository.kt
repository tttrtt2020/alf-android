package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.MatchModel
import com.example.alf.data.model.MatchesPageModel
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchRepository {

    private var matchApiInterface: MatchApiInterface = ApiClient.getApiClient().create(MatchApiInterface::class.java)

    fun fetchMatches(): LiveData<List<MatchModel>> {
        val data = MutableLiveData<List<MatchModel>>()

        matchApiInterface.fetchMatchesPage().enqueue(object : Callback<MatchesPageModel> {

            override fun onFailure(call: Call<MatchesPageModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<MatchesPageModel>,
                response: Response<MatchesPageModel>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res.content
                } else {
                    data.value = null
                }
            }
        })

        return data

    }

    fun fetchMatchById(id: Int): LiveData<MatchModel>? {
        val data = MutableLiveData<MatchModel>()

        matchApiInterface.fetchMatchById(id).enqueue(object : Callback<MatchModel> {

            override fun onFailure(call: Call<MatchModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<MatchModel>,
                response: Response<MatchModel>
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

    suspend fun fetchMatchesPage(nextPageNumber: Int): MatchesPageModel {
        return matchApiInterface.fetchMatchesPage(nextPageNumber)
    }

    /*fun createMatch(personModel: MatchModel):LiveData<MatchModel>{
        val data = MutableLiveData<MatchModel>()

        apiInterface?.createMatch(personModel)?.enqueue(object : Callback<MatchModel>{
            override fun onFailure(call: Call<MatchModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<MatchModel>, response: Response<MatchModel>) {
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

    fun deleteMatch(id:Int):LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()

        apiInterface?.deleteMatch(id)?.enqueue(object : Callback<String>{
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