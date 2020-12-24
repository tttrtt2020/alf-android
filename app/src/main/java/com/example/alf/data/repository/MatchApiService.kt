package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.*
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.network.ApiClient
import com.example.alf.network.MatchApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MatchApiService {

    private var matchApiInterface: MatchApiInterface = ApiClient.getApiClient().create(MatchApiInterface::class.java)

    /*fun fetchMatches(): LiveData<List<Match>> {
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

    }*/

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

    fun fetchMatchTeamSquad(
            squadLiveData: MutableLiveData<List<MatchPlayer>?>,
            matchId: Int,
            teamId: Int
    ): LiveData<List<MatchPlayer>?> {

        matchApiInterface.fetchMatchTeamSquad(matchId, teamId).enqueue(object : Callback<List<MatchPlayer>> {

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
        return matchApiInterface.fetchMatchTeamAllowablePlayers(matchId, teamId, query, sort, nextPageNumber)
    }

    fun addMatchPlayer(
            addPlayerToMatchLiveData: MutableLiveData<Boolean?>,
            matchId: Int,
            teamId: Int,
            fieldPositionId: Int?,
            player: Player
    ): LiveData<Boolean?> {

        matchApiInterface.addMatchPlayer(matchId, teamId, fieldPositionId, player).enqueue(object : Callback<Player> {
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

        matchApiInterface.deleteMatchPlayer(matchId, player.id).enqueue(object : Callback<Unit> {
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