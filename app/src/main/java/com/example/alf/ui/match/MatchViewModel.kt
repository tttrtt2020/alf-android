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

    private var matchApiService: MatchApiService = MatchApiService()

    var matchLiveData: MutableLiveData<Match> = MutableLiveData()

    var hostTeamNameLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.hostTeam.name }
    var guestTeamNameLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.guestTeam.name }
    var hostTeamLogoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildTeamLogoUrl(m.hostTeam) }
    var guestTeamLogoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildTeamLogoUrl(m.guestTeam) }
    var statusLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.status }
    var stadiumPhotoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildStadiumPhotoUrl(m.stadium!!) }
    var resultLiveData: LiveData<String> = Transformations.map(matchLiveData) { m ->
        if (m.status == "FINISHED") (m.resultHostGoals.toString() + ":" + m.resultGuestGoals.toString())
        else "- : -"
    }
    var dateLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> dateFormat.format(m.dateTime) }
    var timeLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> timeFormat.format(m.dateTime) }

    var getMatchResultLiveData: MutableLiveData<Boolean?> = Transformations.map(matchLiveData) { mi -> mi != null } as MutableLiveData<Boolean?>

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

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
