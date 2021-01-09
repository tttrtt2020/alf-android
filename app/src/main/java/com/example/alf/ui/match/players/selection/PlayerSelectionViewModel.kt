package com.example.alf.ui.match.players.selection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Player
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.data.repository.PlayerApiService
import com.example.alf.ui.match.players.PlayersPagingRepository
import kotlinx.coroutines.flow.Flow


class PlayerSelectionViewModel(
    private val playersPagingRepository: PlayersPagingRepository,
    private val matchId: Int,
    private val teamId: Int
) : ViewModel() {

    private var playerApiService: PlayerApiService = PlayerApiService()
    private var eventApiService: EventApiService = EventApiService()

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Player>>? = null

    var selectionResultLiveData: MutableLiveData<Boolean?> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            addSource(selectionResultLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchPlayers(query: String, sort: String): Flow<PagingData<Player>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val flow = playersPagingRepository.getSearchResultStream(matchId, teamId, query, sort)
        val newResult: Flow<PagingData<Player>> = flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    fun addPlayerToMatch(matchId: Int, teamId: Int, fieldPositionId: Int?, player: Player) {
        loadingInProgressLiveData.value = true
        playerApiService.addAppearance(selectionResultLiveData, matchId, teamId, fieldPositionId, player)
    }

    fun addEventToMatch(matchId: Int, event: Event) {
        loadingInProgressLiveData.value = true
        eventApiService.createEvent(selectionResultLiveData, matchId, event)
    }

}
