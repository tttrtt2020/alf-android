package com.example.alf.data.repository

import com.example.alf.data.model.Match
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.MatchesPage
import com.example.alf.data.model.Team
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchApiService {

    private var matchApiInterface: MatchApiInterface = ApiClient.getApiClient().create(MatchApiInterface::class.java)

    fun getMatchById(
            id: Int,
            successCallback: (match: Match) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        matchApiInterface.fetchMatchById(id).enqueue(object : Callback<Match> {

            override fun onFailure(call: Call<Match>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<Match>,
                    response: Response<Match>
            ) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    suspend fun fetchMatchesPage(query: String, sort: String, nextPageNumber: Int): MatchesPage {
        return matchApiInterface.fetchMatchesPage(query, sort, nextPageNumber)
    }

    fun fetchMatchTeam(
            matchId: Int,
            teamId: Int,
            successCallback: (matchTeam: MatchTeam) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        matchApiInterface.fetchMatchTeam(matchId, teamId).enqueue(object : Callback<MatchTeam> {

            override fun onFailure(call: Call<MatchTeam>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<MatchTeam>,
                    response: Response<MatchTeam>
            ) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
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
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}