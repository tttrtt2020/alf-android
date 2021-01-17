package com.example.alf.ui.match.formations

import androidx.lifecycle.*
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.FormationApiService
import com.example.alf.network.Resource

class FormationSelectionViewModel(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModel() {

    private val formationApiService = FormationApiService()

    private val formationsResourceLiveData = MutableLiveData<Resource<List<Formation>>>()
    val formationsLiveData = Transformations.map(formationsResourceLiveData) { resource -> resource.data }
    private val formationsLoadingLiveData = Transformations.map(formationsResourceLiveData) { resource -> resource is Resource.Loading }
    val formationsErrorLiveData = Transformations.map(formationsResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(formationsLiveData) { it != null && it.isEmpty() }

    val addFormationToMatchLiveData = MutableLiveData<Boolean?>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(formationsLoadingLiveData) { loadingInProgressLiveData.value = it }

        getFormations()
    }

    fun getFormations() {
        loadingInProgressLiveData.value = true
        formationApiService.fetchAllowableFormations(
                matchId,
                teamId,
                { formationsResourceLiveData.value = Resource.Success(it) },
                { formationsResourceLiveData.value = Resource.Error(it) }
        )
    }

    fun setFormation(formation: Formation) {
        loadingInProgressLiveData.value = true
        formationApiService.setFormation(addFormationToMatchLiveData, matchId, teamId, formation)
    }

}
