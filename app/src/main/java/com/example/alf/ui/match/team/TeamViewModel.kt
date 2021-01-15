package com.example.alf.ui.match.team

import androidx.lifecycle.*
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.MatchApiService
import com.example.alf.data.repository.PlayerApiService
import com.example.alf.network.Resource

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

    var getSquadResultLiveData: MutableLiveData<Boolean?> = Transformations.map(matchTeamLiveData) { s -> s != null } as MutableLiveData<Boolean?>

    var deletePlayerLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchTeamLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(deletePlayerLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && matchTeamLiveData.value?.appearances?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(matchTeamLiveData) { update() }

            update()
        }

        getSquad()
    }

    fun getSquad() {
        loadingInProgressLiveData.value = true
        matchApiService.fetchMatchTeam(matchTeamResourceLiveData, matchId, teamId)
    }

    fun deletePlayer(player: Player) {
        loadingInProgressLiveData.value = true
        playerApiService.deleteAppearance(deletePlayerLiveData, matchId, player)
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
