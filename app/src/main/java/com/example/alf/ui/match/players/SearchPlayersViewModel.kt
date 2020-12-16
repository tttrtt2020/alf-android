package com.example.alf.ui.match.players

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Player
import com.example.alf.data.repository.MatchApiService
import kotlinx.coroutines.flow.Flow


class SearchPlayersViewModel(
    private val playersPagingRepository: PlayersPagingRepository,
) : ViewModel() {

    private var matchApiService: MatchApiService = MatchApiService()

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Player>>? = null

    var addPlayerToMatchLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            addSource(addPlayerToMatchLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchPlayers(query: String, sort: String): Flow<PagingData<Player>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val pager = playersPagingRepository.getSearchResultPager(query, sort)
        val newResult: Flow<PagingData<Player>> = pager.flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun addPlayerToMatch(matchId: Int, teamId: Int, player: Player) {
        loadingInProgressLiveData.value = true
        matchApiService.addMatchPlayer(addPlayerToMatchLiveData, matchId, teamId, player)
    }

}
