package com.example.alf.ui.match.formations

import androidx.lifecycle.*
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.FormationApiService

class FormationsViewModel(matchId: Int, teamId: Int) : ViewModel() {

    private var formationApiService = FormationApiService()

    var formationsLiveData: MutableLiveData<List<Formation>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(formationsLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && formationsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(formationsLiveData) { update() }

            update()
        }

        fetchFormations(matchId, teamId)
    }

    private fun fetchFormations(matchId: Int, teamId: Int) {
        loadingInProgressLiveData.value = true
        formationApiService.fetchAllowableFormations(formationsLiveData, matchId, teamId)
    }

}
