package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Player
import com.example.alf.data.model.PlayersPage
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.network.ApiClient
import com.example.alf.network.PlayerApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayerApiService {

    private var playerApiInterface: PlayerApiInterface = ApiClient.getApiClient().create(PlayerApiInterface::class.java)

    fun fetchMatchTeamSquad(
        squadLiveData: MutableLiveData<List<MatchPlayer>?>,
        matchId: Int,
        teamId: Int
    ): LiveData<List<MatchPlayer>?> {

        playerApiInterface.fetchMatchTeamSquad(matchId, teamId).enqueue(object :
            Callback<List<MatchPlayer>> {

            override fun onFailure(call: Call<List<MatchPlayer>>, t: Throwable) {
                squadLiveData.value = null
            }

            override fun onResponse(
                call: Call<List<MatchPlayer>>,
                response: Response<List<MatchPlayer>>
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

    fun addMatchPlayer(
        addPlayerToMatchLiveData: MutableLiveData<Boolean?>,
        matchId: Int,
        teamId: Int,
        fieldPositionId: Int?,
        player: Player
    ): LiveData<Boolean?> {

        playerApiInterface.addMatchPlayer(matchId, teamId, fieldPositionId, player).enqueue(object :
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

    fun deleteMatchPlayer(
        resultLiveData: MutableLiveData<Boolean?>,
        matchId: Int,
        player: Player
    ): LiveData<Boolean?> {

        playerApiInterface.deleteMatchPlayer(matchId, player.id).enqueue(object : Callback<Unit> {
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