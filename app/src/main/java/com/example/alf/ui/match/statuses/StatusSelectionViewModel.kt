package com.example.alf.ui.match.statuses

import androidx.lifecycle.*
import com.example.alf.data.model.match.Status
import com.example.alf.data.repository.StatusApiService
import com.example.alf.network.Resource

class StatusSelectionViewModel(
        private val matchId: Int
) : ViewModel() {

    private val statusApiService = StatusApiService()

    private val statusesResourceLiveData = MutableLiveData<Resource<List<Status>>>()
    val statusesLiveData = Transformations.map(statusesResourceLiveData) { resource -> resource.data }
    private val statusesLoadingLiveData = Transformations.map(statusesResourceLiveData) { resource -> resource is Resource.Loading }
    val statusesErrorLiveData = Transformations.map(statusesResourceLiveData) { resource -> resource is Resource.Error }

    val emptyCollectionLiveData = Transformations.map(statusesLiveData) { it != null && it.isEmpty() }

    val setStatusToMatchLiveData = MutableLiveData<Boolean?>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            addSource(statusesLoadingLiveData) { loadingInProgressLiveData.value = it }
            addSource(setStatusToMatchLiveData) { loadingInProgressLiveData.value = false }
        }

        getStatuses()
    }

    fun getStatuses() {
        loadingInProgressLiveData.value = true
        statusApiService.fetchAllowableStatuses(
                matchId,
                { statusesResourceLiveData.value = Resource.Success(it) },
                { statusesResourceLiveData.value = Resource.Error(it) }
        )
    }

    fun setStatus(status: Status) {
        loadingInProgressLiveData.value = true
        statusApiService.setStatus(
                matchId,
                status,
                { setStatusToMatchLiveData.value = true },
                { setStatusToMatchLiveData.value = false }
        )
    }

}
