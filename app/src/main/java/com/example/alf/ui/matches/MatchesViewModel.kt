package com.example.alf.ui.matches

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Match
import com.example.alf.data.repository.MatchApiService
import kotlinx.coroutines.flow.Flow

class MatchesViewModel(
        private val matchesPagingRepository: MatchesPagingRepository
        ) : ViewModel() {

    private var matchApiService: MatchApiService? = null

    private var currentQueryValue: String? = null

    var currentSearchResult: Flow<PagingData<Match>>? = null

    // todo: rework to MediatorLiveData depending on flow or similar
    var loadingInProgressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    init {
        matchApiService = MatchApiService()
    }

    fun searchMatches(query: String, sort: String): Flow<PagingData<Match>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<Match>> = matchesPagingRepository
                .getSearchResultStream(query, sort)
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}