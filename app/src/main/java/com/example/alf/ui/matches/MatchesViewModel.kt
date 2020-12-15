package com.example.alf.ui.matches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.repository.MatchApiService
import kotlinx.coroutines.flow.Flow

class MatchesViewModel(
        private val matchesPagingRepository: MatchesPagingRepository
) : ViewModel() {

    private val pageSize = AlfApplication.getProperty("pagination.matches.pageSize").toInt()

    private var matchApiService: MatchApiService? = null
    var matchListLiveData: LiveData<List<Match>>? = null

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Match>>? = null

    /*val flow = Pager(
            config = PagingConfig(
                    pageSize = pageSize
            )
    ) {
        MatchesPagingSource(MatchesService())
    }.flow.cachedIn(viewModelScope)*/

    // todo: rework to MediatorLiveData depending on flow or similar
    var loadingInProgressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    init {
        matchApiService = MatchApiService()
        matchListLiveData = MutableLiveData()
    }

    fun searchMatches(queryString: String): Flow<PagingData<Match>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Match>> = matchesPagingRepository
                .getSearchResultStream(queryString)
                .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

}