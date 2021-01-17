package com.example.alf.ui.match.referees

import androidx.lifecycle.*
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.RefereeApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class MatchRefereesViewModel(private val matchId: Int) : ViewModel() {

    private val refereeApiService: RefereeApiService = RefereeApiService()

    private val refereesResourceLiveData = MutableLiveData<Resource<List<Referee>>>()
    val refereesLiveData = Transformations.map(refereesResourceLiveData) { resource -> resource.data }
    private val refereesLoadingLiveData = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Loading }
    val refereesErrorLiveData = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(refereesLiveData) { it != null && it.isEmpty() }

    val deleteRefereeActionLiveData = MutableLiveData<ViewEvent<Int>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(refereesLoadingLiveData) { loadingInProgressLiveData.value = it }
        loadingInProgressLiveData.addSource(deleteRefereeActionLiveData) { loadingInProgressLiveData.value = false }

        getReferees()
    }

    fun reset() {
        refereesResourceLiveData.value = refereesResourceLiveData.value
    }

    fun getReferees() {
        refereesResourceLiveData.value = Resource.Loading()
        refereeApiService.fetchMatchReferees(
                matchId,
                { refereesResourceLiveData.value = Resource.Success(it) },
                { refereesResourceLiveData.value = Resource.Error(it) }
        )
    }

    fun deleteReferee(referee: Referee, position: Int) {
        loadingInProgressLiveData.value = true
        refereeApiService.deleteMatchReferee(
                matchId,
                referee,
                { deleteRefereeActionLiveData.value = ViewEvent(position) },
                { deleteRefereeActionLiveData.value = ViewEvent(-1) }
        )
    }

}
