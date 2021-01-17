package com.example.alf.ui.match.referees.selection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.RefereeApiService
import com.example.alf.ui.match.referees.RefereesPagingRepository
import kotlinx.coroutines.flow.Flow


class RefereeSelectionViewModel(
    private val refereesPagingRepository: RefereesPagingRepository,
    private val matchId: Int
) : ViewModel() {

    private val refereeApiService: RefereeApiService = RefereeApiService()

    private var currentQueryValue: String? = null

    var currentSearchResult: Flow<PagingData<Referee>>? = null

    val addRefereeToMatchLiveData = MutableLiveData<Boolean?>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            addSource(addRefereeToMatchLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchReferees(query: String, sort: String): Flow<PagingData<Referee>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val flow = refereesPagingRepository.getSearchResultStream(matchId, query, sort)
        val newResult: Flow<PagingData<Referee>> = flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun addRefereeToMatch(matchId: Int, referee: Referee) {
        loadingInProgressLiveData.value = true
        refereeApiService.addMatchReferee(
                matchId,
                referee,
                { addRefereeToMatchLiveData.value = true },
                { addRefereeToMatchLiveData.value = false }
        )
    }

}
