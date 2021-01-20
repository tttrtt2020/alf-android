package com.example.alf.data.repository

import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Appearance
import com.example.alf.network.ApiClient
import com.example.alf.network.AppearanceApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppearanceApiService {

    private var appearanceApiInterface = ApiClient.getApiClient().create(AppearanceApiInterface::class.java)

    fun fetchMatchTeamAppearances(
            matchId: Int,
            teamId: Int,
            successCallback: (appearances: List<Appearance>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        appearanceApiInterface.fetchMatchTeamAppearances(matchId, teamId).enqueue(object : Callback<List<Appearance>> {

            override fun onFailure(call: Call<List<Appearance>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<List<Appearance>>,
                response: Response<List<Appearance>>
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

    fun addAppearance(
            matchId: Int,
            teamId: Int,
            fieldPositionId: Int?,
            player: Player,
            successCallback: (player: Player) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        appearanceApiInterface.addAppearance(matchId, teamId, fieldPositionId, player).enqueue(object : Callback<Player> {

            override fun onFailure(call: Call<Player>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Player>, response: Response<Player>) {
                if (response.code() == 201) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }
        })
    }

    fun deleteAppearance(
        matchId: Int,
        player: Player,
        successCallback: () -> Unit,
        failureCallback: (errorMessage: String) -> Unit
    ) {
        appearanceApiInterface.deleteAppearance(matchId, player.id).enqueue(object : Callback<Unit> {

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