package com.example.alf.data.repository

import com.example.alf.data.model.Referee
import com.example.alf.data.model.RefereesPage
import com.example.alf.network.ApiClient
import com.example.alf.network.RefereeApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RefereeApiService {

    private var refereeApiInterface: RefereeApiInterface = ApiClient.getApiClient().create(RefereeApiInterface::class.java)

    fun fetchMatchReferees(
            matchId: Int,
            successCallback: (referees: List<Referee>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        refereeApiInterface.fetchMatchReferees(matchId).enqueue(object : Callback<List<Referee>> {

            override fun onFailure(call: Call<List<Referee>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<Referee>>,
                    response: Response<List<Referee>>
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

    suspend fun fetchAllowableMatchRefereesPage(matchId: Int, query: String, sort: String, nextPageNumber: Int): RefereesPage {
        return refereeApiInterface.fetchAllowableMatchRefereesPage(matchId, query, sort, nextPageNumber)
    }

    fun addMatchReferee(
            matchId: Int,
            referee: Referee,
            successCallback: (referee: Referee) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        refereeApiInterface.addMatchReferee(matchId, referee).enqueue(object : Callback<Referee> {

            override fun onFailure(call: Call<Referee>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Referee>, response: Response<Referee>) {
                if (response.code() == 201) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun deleteMatchReferee(
            matchId: Int,
            referee: Referee,
            successCallback: () -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        refereeApiInterface.deleteMatchReferee(matchId, referee.id).enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() == 200 || response.code() == 204) {
                    successCallback()
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}