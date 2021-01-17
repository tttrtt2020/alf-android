package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Player
import com.example.alf.data.model.PlayersPage
import com.example.alf.data.model.match.Appearance
import com.example.alf.network.ApiClient
import com.example.alf.network.PlayerApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import com.example.alf.ui.common.ViewEvent
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerApiService {

    private var playerApiInterface: PlayerApiInterface = ApiClient.getApiClient().create(PlayerApiInterface::class.java)

    fun fetchMatchTeamSquad(
            matchId: Int,
            teamId: Int,
            successCallback: (appearances: List<Appearance>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        playerApiInterface.fetchMatchTeamSquad(matchId, teamId).enqueue(object :
            Callback<List<Appearance>> {

            override fun onFailure(call: Call<List<Appearance>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<List<Appearance>>,
                response: Response<List<Appearance>>
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

    suspend fun fetchMatchTeamAllowablePlayersPage(
        matchId: Int, teamId: Int,
        query: String,
        sort: String, nextPageNumber: Int
    ): PlayersPage {
        return playerApiInterface.fetchMatchTeamAllowablePlayers(
                matchId, teamId,
                query, sort, nextPageNumber
        )
    }

    suspend fun fetchMatchEventAllowablePlayersPage(
        matchId: Int, teamId: Int, eventTypeId: Int, minute: Int,
        query: String,
        sort: String, nextPageNumber: Int
    ): PlayersPage {
        return playerApiInterface.fetchMatchEventAllowablePlayers(
                matchId, teamId, eventTypeId, minute,
                query, sort, nextPageNumber
        )
    }

    fun addAppearance(
            addPlayerToMatchLiveData: MutableLiveData<ViewEvent<Boolean>>,
            matchId: Int,
            teamId: Int,
            fieldPositionId: Int?,
            player: Player
    ): LiveData<ViewEvent<Boolean>> {

        playerApiInterface.addAppearance(matchId, teamId, fieldPositionId, player).enqueue(object :
            Callback<Player> {
            override fun onFailure(call: Call<Player>, t: Throwable) {
                addPlayerToMatchLiveData.value = ViewEvent(false)
            }

            override fun onResponse(call: Call<Player>, response: Response<Player>) {
                addPlayerToMatchLiveData.value = ViewEvent(response.code() == 201)
            }
        })

        return addPlayerToMatchLiveData
    }

    fun deleteAppearance(
        matchId: Int,
        player: Player,
        resultCallback: (result: Boolean) -> Unit
    ) {
        playerApiInterface.deleteAppearance(matchId, player.id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                resultCallback(false)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                val result = response.code() == 200 || response.code() == 204
                resultCallback(result)
            }
        })
    }

}