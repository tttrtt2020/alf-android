package com.example.alf.ui.match.squad

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.alf.data.model.match.MatchPlayer
import com.example.alf.data.repository.MatchApiService

class SquadViewModel(application: Application, matchId: Int, teamId: Int) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService = MatchApiService()

    var squadLiveData: MutableLiveData<List<MatchPlayer>> = MutableLiveData()

    var getSquadResultLiveData: MutableLiveData<Boolean?> = Transformations.map(squadLiveData) { s -> s != null } as MutableLiveData<Boolean?>

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(squadLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && squadLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(squadLiveData) { update() }

            update()
        }

        getSquadByMatchIdAndTeamId(matchId, teamId)
    }

    private fun getSquadByMatchIdAndTeamId(matchId: Int, teamId: Int) {
        loadingInProgressLiveData.value = true
        //squadLiveData.value = null
        matchApiService.fetchMatchTeamSquad(squadLiveData, matchId, teamId)
    }

}
