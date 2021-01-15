package com.example.alf.ui.match.formations

import androidx.lifecycle.*
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.FormationApiService
import com.example.alf.network.Resource

class FormationSelectionViewModel(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModel() {

    private var formationApiService = FormationApiService()

    var formationsResourceLiveData: MutableLiveData<Resource<List<Formation>>> = MutableLiveData()
    var formationsLiveData: LiveData<List<Formation>?> = Transformations.map(formationsResourceLiveData) { resource -> resource.data }
    var formationsLoadingLiveData: LiveData<Boolean> = Transformations.map(formationsResourceLiveData) { resource -> resource is Resource.Loading }
    var formationsErrorLiveData: LiveData<Boolean> = Transformations.map(formationsResourceLiveData) { resource -> resource is Resource.Error }

    var addFormationToMatchLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(formationsLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(addFormationToMatchLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && formationsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(formationsLiveData) { update() }

            update()
        }

        getFormations()
    }

    fun getFormations() {
        loadingInProgressLiveData.value = true
        formationApiService.fetchAllowableFormations(formationsResourceLiveData, matchId, teamId)
    }

    fun setFormation(formation: Formation) {
        loadingInProgressLiveData.value = true
        formationApiService.setFormation(addFormationToMatchLiveData, matchId, teamId, formation)
    }

}
