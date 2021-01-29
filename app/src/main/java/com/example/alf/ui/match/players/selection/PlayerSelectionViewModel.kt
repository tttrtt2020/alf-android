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
import com.example.alf.data.model.Substitution
import com.example.alf.data.model.event.Event
import com.example.alf.data.repository.AppearanceApiService
import com.example.alf.data.repository.EventApiService
import com.example.alf.data.repository.SubstitutionApiService
import com.example.alf.ui.common.ViewEvent
import com.example.alf.ui.match.players.PlayersPagingRepository
import kotlinx.coroutines.flow.Flow


class PlayerSelectionViewModel(
    private val playersPagingRepository: PlayersPagingRepository,
    private val args: PlayerSelectionFragmentArgs
) : ViewModel() {

    private val appearanceApiService = AppearanceApiService()
    private val eventApiService = EventApiService()
    private val substitutionApiService = SubstitutionApiService()

    private var currentQueryValue: String? = null

    var currentSearchResult: Flow<PagingData<Player>>? = null

    val selectionResultLiveData = MutableLiveData<ViewEvent<Player>>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    private var mode: Mode = args.mode

    val message = MutableLiveData<ViewEvent<String>>()
    val navigate = MutableLiveData<ViewEvent<NavDirections>>()

    init {

        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            addSource(selectionResultLiveData) { loadingInProgressLiveData.value = false }
            addSource(message) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchPlayers(query: String, sort: String): Flow<PagingData<Player>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val flow = when (mode) {
            Mode.TEAM_PLAYER -> playersPagingRepository.getSearchResultStreamForAppearance(
                    args.matchId, args.teamId,
                    query, sort
            )
            Mode.EVENT_PLAYER -> playersPagingRepository.getSearchResultStreamForEvent(
                    args.matchId, args.teamId, args.eventType!!.id, args.minute,
                    query, sort
            )
            Mode.SUBSTITUTION_OUT_PLAYER -> playersPagingRepository.getSearchResultStreamForSubstitution(
                    args.matchId, args.teamId, args.minute, Substitution.PlayerType.OUT,
                    query, sort
            )
            Mode.SUBSTITUTION_IN_PLAYER -> playersPagingRepository.getSearchResultStreamForSubstitution(
                    args.matchId, args.teamId, args.minute, Substitution.PlayerType.IN,
                    query, sort
            )
        }
        val newResult: Flow<PagingData<Player>> = flow
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    private fun addPlayerToMatch(matchId: Int, teamId: Int, fieldPositionId: Int?, player: Player) {
        loadingInProgressLiveData.value = true
        appearanceApiService.addAppearance(
                matchId,
                teamId,
                fieldPositionId,
                player,
                { selectionResultLiveData.value = ViewEvent(player) },
                { message.value = ViewEvent(it) }
        )
    }

    private fun addEventToMatch(matchId: Int, event: Event) {
        loadingInProgressLiveData.value = true
        val player = Player(event.person.id, event.person.firstName, event.person.patronymic, event.person.lastName, event.person.birthDate, event.person.country, event.person.height, event.person.weight, null) // todo: fix last null
        eventApiService.createEvent(
                matchId,
                event,
                { selectionResultLiveData.value = ViewEvent(player) },
                { message.value = ViewEvent(it) }
        )
    }

    private fun addSubstitutionToMatch(matchId: Int, substitution: Substitution) {
        loadingInProgressLiveData.value = true
        substitutionApiService.createMatchSubstitution(
                matchId,
                substitution,
                { selectionResultLiveData.value = ViewEvent(substitution.playerIn) },
                { message.value = ViewEvent(it) }
        )
    }

    fun onPlayerSelectionResult(player: Player?) {
        when (mode) {
            Mode.TEAM_PLAYER -> {
                onAddAppearanceResult(player)
            }
            Mode.EVENT_PLAYER -> {
                onAddEventResult(player)
            }
            Mode.SUBSTITUTION_OUT_PLAYER -> {
                onAddSubstitutionOutResult(player)
            }
            Mode.SUBSTITUTION_IN_PLAYER -> {
                onAddSubstitutionInResult(player)
            }
        }
    }

    private fun onAddAppearanceResult(player: Player?) {
        if (player != null) {
            message.value = ViewEvent("Add appearance success")
            navigate.value = ViewEvent(getBackNavDirections(player))
        } else message.value = ViewEvent("Add appearance failed")
    }

    private fun onAddEventResult(player: Player?) {
        if (player != null) {
            message.value = ViewEvent("Add event success")
            navigate.value = ViewEvent(getBackNavDirections(player))
        } else message.value = ViewEvent("Add event failed")
    }

    private fun onAddSubstitutionOutResult(player: Player?) {
        if (player != null) {
            message.value = ViewEvent("Select out player success")
            navigate.value = ViewEvent(getBackNavDirections(player))
        } else message.value = ViewEvent("Select out player failed")
    }

    private fun onAddSubstitutionInResult(player: Player?) {
        if (player != null) {
            message.value = ViewEvent("Add substitution success")
            navigate.value = ViewEvent(getBackNavDirections(player))
        } else message.value = ViewEvent("Add substitution failed")
    }

    private fun getBackNavDirections(player: Player?): NavDirections {
        return when (mode) {
            Mode.TEAM_PLAYER -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToTeamFragment(
                        args.matchId, args.teamId
                )
            }
            Mode.EVENT_PLAYER -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToEventsFragment(
                        args.matchId, args.hostTeamId, args.guestTeamId
                )
            }
            Mode.SUBSTITUTION_OUT_PLAYER -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentSelf(
                        matchId = args.matchId,
                        hostTeamId = args.hostTeamId, guestTeamId = args.guestTeamId,
                        minute = args.minute,
                        teamId = args.teamId, team = args.team,
                        fieldPosition = null,
                        eventType = null,
                        playerOut = player,
                        mode = Mode.SUBSTITUTION_IN_PLAYER
                )
            }
            Mode.SUBSTITUTION_IN_PLAYER -> {
                PlayerSelectionFragmentDirections.actionPlayerSelectionFragmentToSubstitutionsFragment(
                        args.matchId, args.hostTeamId, args.guestTeamId
                )
            }
        }
    }

    fun selectPlayer(player: Player) {
        when (mode) {
            Mode.TEAM_PLAYER -> {
                addPlayerToMatch(
                        args.matchId, args.teamId, args.fieldPosition?.id,
                        player
                )
            }
            Mode.EVENT_PLAYER -> {
                val person = Person(player.id, player.firstName, player.patronymic, player.lastName, player.birthDate, player.country, player.height, player.weight)
                val event = Event(0, args.team!!, person, args.minute, args.eventType!!, null, null)
                addEventToMatch(
                        args.matchId,
                        event
                )
            }
            Mode.SUBSTITUTION_OUT_PLAYER -> {
                //navigate.value = ViewEvent(getBackNavDirections(player))
                selectionResultLiveData.value = ViewEvent(player)
            }
            Mode.SUBSTITUTION_IN_PLAYER -> {
                val substitution = Substitution(
                        null,
                        args.minute,
                        args.team!!,
                        args.playerOut!!,
                        player
                )
                addSubstitutionToMatch(args.matchId, substitution)
            }
        }
    }

}
