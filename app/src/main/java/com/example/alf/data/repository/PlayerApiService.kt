package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Player
import com.example.alf.data.model.PlayersPage
import com.example.alf.data.model.match.Appearance
import com.example.alf.network.ApiClient
import com.example.alf.network.PlayerApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerApiService {

    private var playerApiInterface: PlayerApiInterface = ApiClient.getApiClient().create(PlayerApiInterface::class.java)

    fun fetchMatchTeamSquad(
        squadLiveData: MutableLiveData<List<Appearance>?>,
        matchId: Int,
        teamId: Int
    ): LiveData<List<Appearance>?> {

        playerApiInterface.fetchMatchTeamSquad(matchId, teamId).enqueue(object :
            Callback<List<Appearance>> {

            override fun onFailure(call: Call<List<Appearance>>, t: Throwable) {
                squadLiveData.value = null
            }

            override fun onResponse(
                call: Call<List<Appearance>>,
                response: Response<List<Appearance>>
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

    suspend fun fetchMatchTeamAllowablePlayersPage(
        matchId: Int, teamId: Int,
        query: String,
        sort: String, nextPageNumber: Int
    ): PlayersPage {
        return playerApiInterface.fetchMatchTeamAllowablePlayers(matchId, teamId, query, sort, nextPageNumber)
    }

    fun addAppearance(
        addPlayerToMatchLiveData: MutableLiveData<Boolean?>,
        matchId: Int,
        teamId: Int,
        fieldPositionId: Int?,
        player: Player
    ): LiveData<Boolean?> {

        playerApiInterface.addAppearance(matchId, teamId, fieldPositionId, player).enqueue(object :
            Callback<Player> {
            override fun onFailure(call: Call<Player>, t: Throwable) {
                addPlayerToMatchLiveData.value = false
            }

            override fun onResponse(call: Call<Player>, response: Response<Player>) {
                addPlayerToMatchLiveData.value = response.code() == 201
            }
        })

        return addPlayerToMatchLiveData
    }

    fun deleteAppearance(
        resultLiveData: MutableLiveData<Boolean?>,
        matchId: Int,
        player: Player
    ): LiveData<Boolean?> {

        playerApiInterface.deleteAppearance(matchId, player.id).enqueue(object : Callback<Unit> {
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