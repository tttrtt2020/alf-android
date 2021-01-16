package com.example.alf.ui.match.referees

import androidx.lifecycle.*
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.RefereeApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class MatchRefereesViewModel(private val matchId: Int) : ViewModel() {

    private var refereeApiService: RefereeApiService = RefereeApiService()

    var refereesResourceLiveData: MutableLiveData<Resource<List<Referee>>> = MutableLiveData()
    var refereesLiveData: LiveData<List<Referee>?> = Transformations.map(refereesResourceLiveData) { resource -> resource.data }
    var refereesLoadingLiveData: LiveData<Boolean> = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Loading }
    var refereesErrorLiveData: LiveData<Boolean> = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Error }

    var emptyCollectionLiveData: LiveData<Boolean> = Transformations.map(refereesLiveData) { it != null && it.isEmpty() }

    var deleteRefereeActionLiveData: MutableLiveData<ViewEvent<Int>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

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
        refereeApiService.fetchMatchReferees(refereesResourceLiveData, matchId)
    }

    fun deleteReferee(referee: Referee, position: Int) {
        loadingInProgressLiveData.value = true
        refereeApiService.deleteMatchReferee(matchId, referee) {
            deleteRefereeActionLiveData.value = if (it) ViewEvent(position) else ViewEvent(-1)
        }
    }

}
