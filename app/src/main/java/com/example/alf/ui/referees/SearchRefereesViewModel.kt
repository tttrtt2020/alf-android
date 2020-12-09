package com.example.alf.ui.referees

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Referee
import com.example.alf.data.repository.RefereeApiService
import kotlinx.coroutines.flow.Flow


class SearchRefereesViewModel(
    private val refereesPagingRepository: RefereesPagingRepository
) : ViewModel() {

    private var refereeApiService: RefereeApiService = RefereeApiService()
    var refereeListLiveData: LiveData<List<Referee>> = MutableLiveData()

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Referee>>? = null

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

}
