package com.example.alf.ui.match.referees

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.MatchApiService

class MatchRefereesViewModel(private val matchId: Int) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()

    var refereesLiveData: MutableLiveData<List<Referee>?> = MutableLiveData()

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
        matchApiService.fetchMatchReferees(refereesLiveData, matchId)
    }

    fun deleteReferee(referee: Referee) {
        loadingInProgressLiveData.value = true
        matchApiService.deleteMatchReferee(deleteRefereeLiveData, matchId, referee)
    }

}
