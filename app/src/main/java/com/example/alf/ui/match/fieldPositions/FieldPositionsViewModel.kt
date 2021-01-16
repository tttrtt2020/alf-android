package com.example.alf.ui.match.fieldPositions

import androidx.lifecycle.*
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.data.repository.FieldPositionApiService
import com.example.alf.network.Resource

class FieldPositionsViewModel(
        private val matchId: Int,
        private val teamId: Int
) : ViewModel() {

    private val fieldPositionApiService = FieldPositionApiService()

    private val fieldPositionsResourceLiveData = MutableLiveData<Resource<List<FieldPosition>>>()
    val fieldPositionsLiveData = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource.data }
    private val fieldPositionsLoadingLiveData = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource is Resource.Loading }
    val fieldPositionsErrorLiveData = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(fieldPositionsLiveData) { it != null && it.isEmpty() }

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(fieldPositionsLoadingLiveData) { loadingInProgressLiveData.value = it }

        getFieldPositions()
    }

    fun getFieldPositions() {
        fieldPositionApiService.fetchFreeFieldPositions(fieldPositionsResourceLiveData, matchId, teamId)
    }

}
