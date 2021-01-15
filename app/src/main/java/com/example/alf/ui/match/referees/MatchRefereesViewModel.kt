package com.example.alf.ui.match.referees

import androidx.lifecycle.*
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.RefereeApiService
import com.example.alf.network.Resource

class MatchRefereesViewModel(private val matchId: Int) : ViewModel() {

    private var refereeApiService: RefereeApiService = RefereeApiService()

    var refereesResourceLiveData: MutableLiveData<Resource<List<Referee>>> = MutableLiveData()
    var refereesLiveData: LiveData<List<Referee>?> = Transformations.map(refereesResourceLiveData) { resource -> resource.data }
    var refereesLoadingLiveData: LiveData<Boolean> = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Loading }
    var refereesErrorLiveData: LiveData<Boolean> = Transformations.map(refereesResourceLiveData) { resource -> resource is Resource.Error }

    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    var deleteRefereeLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(refereesLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(deleteRefereeLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && refereesLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(refereesLiveData) { update() }

            update()
        }

        getReferees()
    }

    fun getReferees() {
        loadingInProgressLiveData.value = true
        refereeApiService.fetchMatchReferees(refereesResourceLiveData, matchId)
    }

    fun deleteReferee(referee: Referee) {
        loadingInProgressLiveData.value = true
        refereeApiService.deleteMatchReferee(deleteRefereeLiveData, matchId, referee)
    }

}
