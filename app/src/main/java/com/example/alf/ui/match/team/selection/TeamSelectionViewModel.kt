package com.example.alf.ui.match.team.selection

import androidx.lifecycle.*
import com.example.alf.data.model.Team
import com.example.alf.data.repository.MatchApiService

class TeamSelectionViewModel(
        private val matchId: Int
) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()

    var teamsLiveData: MutableLiveData<List<Team>?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(teamsLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && teamsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(teamsLiveData) { update() }

            update()
        }

        getTeams()
    }

    private fun getTeams() {
        loadingInProgressLiveData.value = true
        matchApiService.fetchTeams(teamsLiveData, matchId)
    }

}
