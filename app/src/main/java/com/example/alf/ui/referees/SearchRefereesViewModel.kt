package com.example.alf.ui.referees

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.MatchApiService
import kotlinx.coroutines.flow.Flow


class SearchRefereesViewModel(
    private val refereesPagingRepository: RefereesPagingRepository,
) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Referee>>? = null

    var addRefereeToMatchLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            addSource(addRefereeToMatchLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchReferees(queryString: String): Flow<PagingData<Referee>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val pager = refereesPagingRepository.getSearchResultPager(queryString)
        val newResult: Flow<PagingData<Referee>> = pager.flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun addRefereeToMatch(matchId: Int, referee: Referee) {
        loadingInProgressLiveData.value = true
        matchApiService.addMatchReferee(addRefereeToMatchLiveData, matchId, referee)
    }

}
