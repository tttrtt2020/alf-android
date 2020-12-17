package com.example.alf.ui.match.team

import android.app.Application
import androidx.lifecycle.*
import com.example.alf.data.model.MatchTeam
import com.example.alf.data.model.Player
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.MatchApiService

class TeamViewModel(application: Application, matchId: Int, teamId: Int) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService = MatchApiService()

    var matchTeamLiveData: MutableLiveData<MatchTeam> = MutableLiveData()

    var formationLiveData: LiveData<Formation?> = Transformations.map(matchTeamLiveData) { sq -> sq.formation }

    var getSquadResultLiveData: MutableLiveData<Boolean?> = Transformations.map(matchTeamLiveData) { s -> s != null } as MutableLiveData<Boolean?>

    var deleteMatchPlayerLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchTeamLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(deleteMatchPlayerLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && matchTeamLiveData.value?.matchPlayers?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(matchTeamLiveData) { update() }

            update()
        }

        getSquadByMatchIdAndTeamId(matchId, teamId)
    }

    fun getSquadByMatchIdAndTeamId(matchId: Int, teamId: Int) {
        loadingInProgressLiveData.value = true
        //squadLiveData.value = null
        matchApiService.fetchMatchTeam(matchTeamLiveData, matchId, teamId)
    }

    fun deleteMatchPlayer(matchId: Int, player: Player) {
        loadingInProgressLiveData.value = true
        matchApiService.deleteMatchPlayer(deleteMatchPlayerLiveData, matchId, player)
    }

}