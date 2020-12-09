package com.example.alf.ui.match.referees

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.MatchApiService

class MatchRefereesViewModel(application: Application, matchId: Int) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService = MatchApiService()

    var matchRefereesLiveData: MutableLiveData<List<Referee>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchRefereesLiveData) { loadingInProgressLiveData.value = false }

        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && matchRefereesLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(matchRefereesLiveData) { update() }

            update()
        }

        getRefereesByMatchId(matchId)
    }

    private fun getRefereesByMatchId(matchId: Int) {
        loadingInProgressLiveData.value = true
        matchApiService.fetchMatchReferees(matchRefereesLiveData, matchId)
    }

}
