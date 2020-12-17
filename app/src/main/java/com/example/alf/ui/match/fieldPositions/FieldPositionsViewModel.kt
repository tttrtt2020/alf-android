package com.example.alf.ui.match.fieldPositions

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.data.repository.FieldPositionApiService

class FieldPositionsViewModel(matchId: Int, teamId: Int) : ViewModel() {

    private var fieldPositionApiService: FieldPositionApiService = FieldPositionApiService()
    var fieldPositionsLiveData: MutableLiveData<List<FieldPosition>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(fieldPositionsLiveData) { loadingInProgressLiveData.value = false }

        fetchFieldPositions(matchId, teamId)
    }

    private fun fetchFieldPositions(matchId: Int, teamId: Int) {
        fieldPositionApiService.fetchAllFieldPositions(fieldPositionsLiveData, matchId, teamId)
    }

}
