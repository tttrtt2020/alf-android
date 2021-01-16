package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Match
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.Team
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
import com.example.alf.network.Resource
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchApiService {

    private var matchApiInterface: MatchApiInterface = ApiClient.getApiClient().create(MatchApiInterface::class.java)

    fun getMatchById(
            matchLiveData: MutableLiveData<Match>,
            id: Int
    ): LiveData<Match> {

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

    suspend fun fetchMatchesPage(query: String, sort: String, nextPageNumber: Int): MatchesPage {
        return matchApiInterface.fetchMatchesPage(query, sort, nextPageNumber)
    }

    fun fetchMatchTeam(
            matchTeamLiveData: MutableLiveData<Resource<MatchTeam>>,
            matchId: Int,
            teamId: Int
    ): LiveData<Resource<MatchTeam>> {

        matchApiInterface.fetchMatchTeam(matchId, teamId).enqueue(object : Callback<MatchTeam> {

            override fun onFailure(call: Call<MatchTeam>, t: Throwable) {
                matchTeamLiveData.value = Resource.Error(t.localizedMessage!!, null)
            }

            override fun onResponse(
                    call: Call<MatchTeam>,
                    response: Response<MatchTeam>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    matchTeamLiveData.value = Resource.Success(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    matchTeamLiveData.value = Resource.Error(apiError.message, null)
                }
            }
        })

        return matchTeamLiveData
    }

    fun fetchTeams(
            matchId: Int,
            successCallback: (eventTypes: List<Team>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        matchApiInterface.fetchTeams(matchId).enqueue(object : Callback<List<Team>> {

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<Team>>,
                    response: Response<List<Team>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    successCallback(res)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }
        })
    }

}