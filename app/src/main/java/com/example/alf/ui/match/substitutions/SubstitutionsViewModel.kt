package com.example.alf.ui.match.substitutions

import androidx.lifecycle.*
import com.example.alf.AlfApplication
import com.example.alf.data.model.Substitution
import com.example.alf.data.repository.SubstitutionApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent

class SubstitutionsViewModel(private val matchId: Int) : ViewModel() {

    private val substitutionApiService: SubstitutionApiService = SubstitutionApiService()

    private val substitutionsResourceLiveData = MutableLiveData<Resource<List<Substitution>>>()
    val substitutionsLiveData = Transformations.map(substitutionsResourceLiveData) { resource -> resource.data }
    private val substitutionsLoadingLiveData = Transformations.map(substitutionsResourceLiveData) { resource -> resource is Resource.Loading }
    val substitutionsErrorLiveData = Transformations.map(substitutionsResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(substitutionsLiveData) { it != null && it.isEmpty() }

    val deleteSubstitutionActionLiveData = MutableLiveData<ViewEvent<Int>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(substitutionsLoadingLiveData) { loadingInProgressLiveData.value = it }
        loadingInProgressLiveData.addSource(deleteSubstitutionActionLiveData) { loadingInProgressLiveData.value = false }

        getSubstitutions()
    }

    fun reset() {
        substitutionsResourceLiveData.value = substitutionsResourceLiveData.value
    }

    fun getSubstitutions() {
        substitutionsResourceLiveData.value = Resource.Loading()
        substitutionApiService.fetchMatchSubstitutions(
                matchId,
                AlfApplication.getProperty("substitutions.sort"),
                AlfApplication.getProperty("substitutions.sort2"),
                { substitutionsResourceLiveData.value = Resource.Success(it) },
                { substitutionsResourceLiveData.value = Resource.Error(it) }
        )
    }

    fun deleteSubstitution(substitution: Substitution, position: Int) {
        loadingInProgressLiveData.value = true
        substitutionApiService.deleteMatchSubstitution(
                matchId,
                substitution.id!!,
                { deleteSubstitutionActionLiveData.value = ViewEvent(position) },
                { deleteSubstitutionActionLiveData.value = ViewEvent(-1) }
        )
    }

}
