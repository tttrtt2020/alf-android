package com.example.alf.ui.match.team

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.MatchApiService
import com.example.alf.data.repository.PlayerApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class TeamViewModel(
        private val matchId: Int,
        private val teamId: Int
) : ViewModel() {

    private val matchApiService: MatchApiService = MatchApiService()
    private val playerApiService: PlayerApiService = PlayerApiService()

    private val matchTeamResourceLiveData = MutableLiveData<Resource<MatchTeam>>()
    private val matchTeamLiveData = Transformations.map(matchTeamResourceLiveData) { resource -> resource.data }
    private val matchTeamLoadingLiveData = Transformations.map(matchTeamResourceLiveData) { resource -> resource is Resource.Loading }
    val matchTeamErrorLiveData = Transformations.map(matchTeamResourceLiveData) { resource -> resource is Resource.Error }

    val titleLiveData = Transformations.map(matchTeamLiveData) { mt ->
        if (mt != null ) mt.team.name + (if (mt.formation != null) (": " + mt.formation!!.name) else "") else null
    }
    val formationLiveData = Transformations.map(matchTeamLiveData) { sq -> sq?.formation }
    val squadLiveData = Transformations.map(matchTeamLiveData) { it?.appearances }
    val emptyCollectionLiveData = Transformations.map(squadLiveData) { it != null && it.isEmpty() }

    val deletePlayerActionLiveData = MutableLiveData<ViewEvent<Int>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchTeamLoadingLiveData) { loadingInProgressLiveData.value = it }
        loadingInProgressLiveData.addSource(deletePlayerActionLiveData) { loadingInProgressLiveData.value = false }

        getSquad()
    }

    fun reset() {
        matchTeamResourceLiveData.value = matchTeamResourceLiveData.value
    }

    fun getSquad() {
        matchApiService.fetchMatchTeam(matchTeamResourceLiveData, matchId, teamId)
    }

    fun deletePlayer(player: Player, position: Int) {
        loadingInProgressLiveData.value = true
        playerApiService.deleteAppearance(matchId, player) {
            deletePlayerActionLiveData.value = if (it) ViewEvent(position) else ViewEvent(-1)
        }
    }

    fun replacePlayer(player: Player) {
        TODO("Not yet implemented")
    }

    fun freeFieldPositionsExist(formation: Formation?): Boolean {
        return if (formation == null) {
            false
        } else matchTeamLiveData.value?.appearances?.count { mp -> mp.fieldPosition != null }!! < formation.fieldPositions.size
    }

}
