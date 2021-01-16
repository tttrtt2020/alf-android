package com.example.alf.ui.match.team.selection

import androidx.lifecycle.*
import com.example.alf.data.model.Team
import com.example.alf.data.repository.MatchApiService
import com.example.alf.network.Resource

class TeamSelectionViewModel(
        private val matchId: Int
) : ViewModel() {

    private val matchApiService: MatchApiService = MatchApiService()

    private val teamsResourceLiveData = MutableLiveData<Resource<List<Team>>>()
    val teamsLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource.data }
    private val teamsLoadingLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource is Resource.Loading }
    val teamsErrorLiveData = Transformations.map(teamsResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(teamsLiveData) { it != null && it.isEmpty() }

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

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
