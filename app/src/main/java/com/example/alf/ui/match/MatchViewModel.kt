package com.example.alf.ui.match

import android.app.Application
import androidx.lifecycle.*
import com.example.alf.AlfApplication
import com.example.alf.data.model.event.Event
import com.example.alf.data.model.Team
import com.example.alf.data.model.match.Match
import com.example.alf.data.model.match.MatchInfo
import com.example.alf.data.model.match.MatchPerson
import com.example.alf.data.model.match.Stadium
import com.example.alf.data.repository.MatchApiService
import java.text.SimpleDateFormat
import java.util.*

class MatchViewModel(application: Application, id: Int) : AndroidViewModel(application) {

    private val dateFormat = SimpleDateFormat(AlfApplication.getProperty("match.dateFormat"), Locale.getDefault())
    private val timeFormat = SimpleDateFormat(AlfApplication.getProperty("match.timeFormat"), Locale.getDefault())

    private var matchApiService: MatchApiService = MatchApiService()

    var matchInfoLiveData: MutableLiveData<MatchInfo> = MutableLiveData()

    var matchLiveData: LiveData<Match> = Transformations.map(matchInfoLiveData) { mi -> mi.mainInfo.match }
    var hostTeamNameLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.hostTeam.name }
    var guestTeamNameLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.guestTeam.name }
    var statusLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.status }
    var stadiumPhotoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildStadiumPhotoUrl(m.stadium!!) }
    var resultLiveData: LiveData<String> = Transformations.map(matchLiveData) { m ->
        if (m.status == "FINISHED") (m.result.hostGoals.toString() + ":" + m.result.guestGoals.toString())
        else "- : -"
    }
    var dateLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> dateFormat.format(m.dateTime) }
    var timeLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> timeFormat.format(m.dateTime) }
    var eventsLiveData: LiveData<List<Event>> = Transformations.map(matchInfoLiveData) { mi ->
        mi.mainInfo.hostEvents.plus(mi.mainInfo.guestEvents)
    }
    var hostEventsLiveData: LiveData<List<Event>> = Transformations.map(matchInfoLiveData) { mi -> mi.mainInfo.hostEvents }
    var guestEventsLiveData: LiveData<List<Event>> = Transformations.map(matchInfoLiveData) { mi -> mi.mainInfo.guestEvents }
    var hostTeamLogoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildTeamLogoUrl(m.hostTeam) }
    var guestTeamLogoUrlLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> buildTeamLogoUrl(m.guestTeam) }
    var hostSquadLiveData: LiveData<List<MatchPerson>> = Transformations.map(matchInfoLiveData) { mi -> mi.squadsInfo.hostSquad }
    var guestSquadLiveData: LiveData<List<MatchPerson>> = Transformations.map(matchInfoLiveData) { mi -> mi.squadsInfo.guestSquad }

    var getMatchInfoResultLiveData: MutableLiveData<Boolean?> = Transformations.map(matchInfoLiveData) { mi -> mi != null } as MutableLiveData<Boolean?>

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchInfoLiveData) { loadingInProgressLiveData.value = false }

        getMatchInfoById(id)
    }

    private fun buildStadiumPhotoUrl(stadium: Stadium): String {
        return AlfApplication.getProperty("url.image.stadium.background") +
                stadium.id + AlfApplication.getProperty("extension.stadium.background")
    }

    private fun buildTeamLogoUrl(team: Team): String {
        return AlfApplication.getProperty("url.logo.club") + team.clubId + AlfApplication.getProperty("extension.logo.club")
    }

    fun getMatchInfoById(id: Int) {
        loadingInProgressLiveData.value = true
        //matchInfoLiveData.value = null
        matchApiService.getMatchInfoById(matchInfoLiveData, id)
    }

}
