package com.example.alf.ui.match.team.selection

import androidx.lifecycle.*
import com.example.alf.data.model.Team
import com.example.alf.data.repository.MatchApiService
import com.example.alf.network.Resource

class TeamSelectionViewModel(
        private val matchId: Int
) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()

    var teamsResourceLiveData = MutableLiveData<Resource<List<Team>>>()
    var teamsLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource.data }
    var teamsLoadingLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource is Resource.Loading }
    var teamsErrorLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource is Resource.Error }

    var emptyCollectionLiveData: LiveData<Boolean> = Transformations.map(teamsLiveData) { it != null && it.isEmpty() }

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(teamsLoadingLiveData) { loadingInProgressLiveData.value = it }

        getTeams()
    }

    fun getTeams() {
        loadingInProgressLiveData.value = true
        matchApiService.fetchTeams(
                matchId,
                { teamsResourceLiveData.value = Resource.Success(it) },
                { teamsResourceLiveData.value = Resource.Error(it) }
        )
    }

}
