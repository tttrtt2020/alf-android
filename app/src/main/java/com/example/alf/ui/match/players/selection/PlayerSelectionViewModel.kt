package com.example.alf.ui.match.players.selection

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDirections
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Person
import com.example.alf.data.model.Player
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.EventApiService
import com.example.alf.data.repository.PlayerApiService
import com.example.alf.ui.common.ViewEvent
import com.example.alf.ui.match.players.PlayersPagingRepository
import kotlinx.coroutines.flow.Flow


class PlayerSelectionViewModel(
    private val playersPagingRepository: PlayersPagingRepository,
    private val args: PlayerSelectionFragmentArgs
) : ViewModel() {

    private var playerApiService: PlayerApiService = PlayerApiService()
    private var eventApiService: EventApiService = EventApiService()

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Player>>? = null

    var selectionResultLiveData: MutableLiveData<ViewEvent<Boolean>> = MutableLiveData()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    private var mode: Mode

    val message = MutableLiveData<ViewEvent<String>>()
    val goBack = MutableLiveData<ViewEvent<NavDirections>>()

    init {
        mode = if (args.eventType == null) {
            // selection of player for addition to team
            Mode.TO_TEAM
        } else {
            // selection of player for event
            Mode.TO_EVENT
        }

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
        val flow = playersPagingRepository.getSearchResultStream(args.matchId, args.teamId, query, sort)
        val newResult: Flow<PagingData<Player>> = flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private fun addPlayerToMatch(matchId: Int, teamId: Int, fieldPositionId: Int?, player: Player) {
        loadingInProgressLiveData.value = true
        playerApiService.addAppearance(selectionResultLiveData, matchId, teamId, fieldPositionId, player)
    }

    private fun addEventToMatch(matchId: Int, event: Event) {
        loadingInProgressLiveData.value = true
        eventApiService.createEvent(selectionResultLiveData, matchId, event)
    }

    fun onPlayerSelectionResult(success: Boolean) {
        if (mode == Mode.TO_TEAM) {
            onAddAppearanceResult(success)
        } else if (mode == Mode.TO_EVENT) {
            onAddEventResult(success)
        }
    }

    private fun onAddAppearanceResult(success: Boolean) {
        if (success) {
            message.value = ViewEvent("Add appearance success")
            goBack.value = ViewEvent(getBackNavDirections())
        } else message.value = ViewEvent("Add appearance failed")
    }

    private fun onAddEventResult(success: Boolean) {
        if (success) {
            message.value = ViewEvent("Add event success")
            goBack.value = ViewEvent(getBackNavDirections())
        } else message.value = ViewEvent("Add event failed")
    }

    private fun getBackNavDirections(): NavDirections {
        return when (mode) {
            Mode.TO_TEAM -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToTeamFragment(
                        args.matchId, args.teamId
                )
            }
            Mode.TO_EVENT -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToEventsFragment(
                        args.matchId, args.hostTeamId, args.guestTeamId
                )
            }
        }
    }

    fun selectPlayer(player: Player) {
        if (mode == Mode.TO_TEAM) {
            addPlayerToMatch(
                    args.matchId, args.teamId, args.fieldPosition?.id,
                    player
            )
        } else if (mode == Mode.TO_EVENT) {
            val person = Person(player.id, player.firstName, player.patronymic, player.lastName, player.birthDate, player.country, player.height, player.weight)
            val event = Event(0, args.team!!, person, args.minute, args.eventType!!, null, null)
            addEventToMatch(
                    args.matchId,
                    event
            )
        }
    }

    enum class Mode {
        TO_TEAM, TO_EVENT
    }

}
