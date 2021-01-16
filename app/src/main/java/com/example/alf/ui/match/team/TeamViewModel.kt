package com.example.alf.ui.match.team

import androidx.lifecycle.*
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Appearance
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.MatchApiService
import com.example.alf.data.repository.PlayerApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class TeamViewModel(
        private val matchId: Int,
        private val teamId: Int
) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()
    private var playerApiService: PlayerApiService = PlayerApiService()

    var matchTeamResourceLiveData: MutableLiveData<Resource<MatchTeam>> = MutableLiveData()
    var matchTeamLiveData: LiveData<MatchTeam?> = Transformations.map(matchTeamResourceLiveData) { resource -> resource.data }
    var matchTeamLoadingLiveData: LiveData<Boolean> = Transformations.map(matchTeamResourceLiveData) { resource -> resource is Resource.Loading }
    var matchTeamErrorLiveData: LiveData<Boolean> = Transformations.map(matchTeamResourceLiveData) { resource -> resource is Resource.Error }

    var titleLiveData: LiveData<String?> = Transformations.map(matchTeamLiveData) { mt ->
        if (mt != null ) mt.team.name + (if (mt.formation != null) (": " + mt.formation!!.name) else "") else null
    }
    var formationLiveData: LiveData<Formation?> = Transformations.map(matchTeamLiveData) { sq -> sq?.formation }
    var squadLiveData: LiveData<List<Appearance>?> = Transformations.map(matchTeamLiveData) { it?.appearances }
    var emptyCollectionLiveData: LiveData<Boolean> = Transformations.map(squadLiveData) { it != null && it.isEmpty() }

    var deletePlayerActionLiveData: MutableLiveData<ViewEvent<Int>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

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
