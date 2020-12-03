package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Match
import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchApiService {

    private var matchApiInterface: MatchApiInterface = ApiClient.getApiClient().create(MatchApiInterface::class.java)

    fun fetchMatches(): LiveData<List<Match>> {
        val data = MutableLiveData<List<Match>>()

        matchApiInterface.fetchMatchesPage().enqueue(object : Callback<MatchesPage> {

            override fun onFailure(call: Call<MatchesPage>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                    call: Call<MatchesPage>,
                    response: Response<MatchesPage>
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

    fun getMatchById(matchLiveData: MutableLiveData<Match>, id: Int): LiveData<Match> {

        matchApiInterface.fetchMatchById(id).enqueue(object : Callback<Match> {

            override fun onFailure(call: Call<Match>, t: Throwable) {
                matchLiveData.value = null
            }

            override fun onResponse(
                    call: Call<Match>,
                    response: Response<Match>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    matchLiveData.value = res
                } else {
                    matchLiveData.value = null
                }
            }
        })

        return matchLiveData
    }

    suspend fun fetchMatchesPage(nextPageNumber: Int): MatchesPage {
        return matchApiInterface.fetchMatchesPage(nextPageNumber)
    }

    /*fun createMatch(personModel: Match):LiveData<Match>{
        val data = MutableLiveData<Match>()

        apiInterface?.createMatch(person)?.enqueue(object : Callback<Match>{
            override fun onFailure(call: Call<Match>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Match>, response: Response<Match>) {
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

    fun fetchMatchTeamSquad(squadLiveData: MutableLiveData<List<MatchPerson>>, matchId: Int, teamId: Int): LiveData<List<MatchPerson>> {

        matchApiInterface.fetchMatchTeamSquad(matchId, teamId).enqueue(object : Callback<List<MatchPerson>> {

            override fun onFailure(call: Call<List<MatchPerson>>, t: Throwable) {
                squadLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<MatchPerson>>,
                    response: Response<List<MatchPerson>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    squadLiveData.value = res
                } else {
                    squadLiveData.value = null
                }
            }
        })

        return squadLiveData
    }

}