package com.example.alf.ui.match

import androidx.lifecycle.*
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.model.Stadium
import com.example.alf.data.model.Team
import com.example.alf.data.repository.MatchApiService
import java.text.SimpleDateFormat
import java.util.*

class MatchViewModel(private val matchId: Int) : ViewModel() {

    private val dateFormat = SimpleDateFormat(AlfApplication.getProperty("match.dateFormat"), Locale.getDefault())
    private val timeFormat = SimpleDateFormat(AlfApplication.getProperty("match.timeFormat"), Locale.getDefault())

    private val matchApiService: MatchApiService = MatchApiService()

    val matchLiveData = MutableLiveData<Resource<Match>>()

    val hostTeamLiveData = Transformations.map(matchLiveData) { m -> m.data?.hostTeam }
    val guestTeamLiveData = Transformations.map(matchLiveData) { m -> m.data?.guestTeam }
    val hostTeamNameLiveData = Transformations.map(hostTeamLiveData) { t -> t?.name }
    val guestTeamNameLiveData = Transformations.map(guestTeamLiveData) { t -> t?.name }
    val hostTeamLogoUrlLiveData = Transformations.map(hostTeamLiveData) { t -> t?.let { buildTeamLogoUrl(it) } }
    val guestTeamLogoUrlLiveData = Transformations.map(guestTeamLiveData) { t -> t?.let { buildTeamLogoUrl(it) } }
    val statusLiveData = Transformations.map(matchLiveData) { m -> m.data?.status }
    val stadiumPhotoUrlLiveData = Transformations.map(matchLiveData) { m -> m.data?.stadium?.let { buildStadiumPhotoUrl(it) } }
    val resultLiveData = Transformations.map(matchLiveData) { m ->
        if (m.data?.status == "FINISHED") (m.data.resultHostGoals.toString() + ":" + m.data.resultGuestGoals.toString())
        else "- : -"
    }
    val dateLiveData = Transformations.map(matchLiveData) { m -> m.data?.dateTime?.let { dateFormat.format(m.data.dateTime!!) } }
    val timeLiveData = Transformations.map(matchLiveData) { m -> m.data?.dateTime?.let { timeFormat.format(m.data.dateTime!!) } }

    val getMatchResultLiveData = Transformations.map(matchLiveData) { mi -> mi != null } as MutableLiveData<Boolean?>

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchLiveData) { loadingInProgressLiveData.value = false }

        getMatch()
    }

    private fun buildStadiumPhotoUrl(stadium: Stadium): String {
        return AlfApplication.getProperty("url.image.stadium.background") +
                stadium.id + AlfApplication.getProperty("extension.stadium.background")
    }

    private fun buildTeamLogoUrl(team: Team): String {
        return AlfApplication.getProperty("url.logo.club") + team.club.id + AlfApplication.getProperty("extension.logo.club")
    }

    private fun getMatch() {
        loadingInProgressLiveData.value = true
        //matchLiveData.value = null
        matchApiService.getMatchById(matchLiveData, matchId)
    }

}
