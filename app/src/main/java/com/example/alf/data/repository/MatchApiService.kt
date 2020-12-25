package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Match
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.Team
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
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
            matchTeamLiveData: MutableLiveData<MatchTeam?>,
            matchId: Int,
            teamId: Int
    ): LiveData<MatchTeam?> {

        matchApiInterface.fetchMatchTeam(matchId, teamId).enqueue(object : Callback<MatchTeam> {

            override fun onFailure(call: Call<MatchTeam>, t: Throwable) {
                matchTeamLiveData.value = null
            }

            override fun onResponse(
                    call: Call<MatchTeam>,
                    response: Response<MatchTeam>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    matchTeamLiveData.value = res
                } else {
                    matchTeamLiveData.value = null
                }
            }
        })

        return matchTeamLiveData
    }

    fun fetchTeams(
            teamsLiveData: MutableLiveData<List<Team>?>,
            matchId: Int
    ): LiveData<List<Team>?> {

        matchApiInterface.fetchTeams(matchId).enqueue(object : Callback<List<Team>> {

            override fun onFailure(call: Call<List<Team>>, t: Throwable) {
                teamsLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<Team>>,
                    response: Response<List<Team>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    teamsLiveData.value = res
                } else {
                    teamsLiveData.value = null
                }
            }
        })

        return teamsLiveData
    }

}