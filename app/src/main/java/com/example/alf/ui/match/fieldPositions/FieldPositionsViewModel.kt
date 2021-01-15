package com.example.alf.ui.match.fieldPositions

import androidx.lifecycle.*
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.data.repository.FieldPositionApiService
import com.example.alf.network.Resource

class FieldPositionsViewModel(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModel() {

    private var fieldPositionApiService = FieldPositionApiService()

    var fieldPositionsResourceLiveData: MutableLiveData<Resource<List<FieldPosition>>> = MutableLiveData()
    var fieldPositionsLiveData: LiveData<List<FieldPosition>?> = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource.data }
    var fieldPositionsLoadingLiveData: LiveData<Boolean> = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource is Resource.Loading }
    var fieldPositionsErrorLiveData: LiveData<Boolean> = Transformations.map(fieldPositionsResourceLiveData) { resource -> resource is Resource.Error }

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var emptyCollectionLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(fieldPositionsLiveData) { loadingInProgressLiveData.value = false }
        emptyCollectionLiveData.apply {
            fun update() {
                value = loadingInProgressLiveData.value == false && fieldPositionsLiveData.value?.isEmpty() ?: false
            }

            addSource(loadingInProgressLiveData) { update() }
            addSource(fieldPositionsLiveData) { update() }

            update()
        }

        getFieldPositions()
    }

    fun getFieldPositions() {
        loadingInProgressLiveData.value = true
        fieldPositionApiService.fetchFreeFieldPositions(fieldPositionsResourceLiveData, matchId, teamId)
    }

}
