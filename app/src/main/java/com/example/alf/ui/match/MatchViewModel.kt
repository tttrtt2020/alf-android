package com.example.alf.ui.match

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.model.Stadium
import com.example.alf.data.model.Team
import com.example.alf.data.repository.MatchApiService
import com.example.alf.network.Resource
import com.example.alf.ui.common.ViewEvent
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
    val competitionNameLiveData = Transformations.map(matchLiveData) { m -> m.data?.competitionName }
    val formatLiveData = Transformations.map(matchLiveData) { m -> m.data?.format }
    val stadiumLiveData = Transformations.map(matchLiveData) { m -> m.data?.stadium }
    val statusLiveData = Transformations.map(matchLiveData) { m -> m.data?.status }
    val stadiumPhotoUrlLiveData = Transformations.map(matchLiveData) { m -> m.data?.stadium?.let { buildStadiumPhotoUrl(it) } }
    val scoreLiveData = Transformations.map(matchLiveData) { m ->
        if (m.data?.status?.name == "FINISHED") (m.data.scoreHost.toString() + ":" + m.data.scoreGuest.toString())
        else "- : -"
    }
    val dateLiveData = Transformations.map(matchLiveData) { m -> m.data?.dateTime?.let { dateFormat.format(m.data.dateTime!!) } }
    val timeLiveData = Transformations.map(matchLiveData) { m -> m.data?.dateTime?.let { timeFormat.format(m.data.dateTime!!) } }

    val getMatchResultLiveData = Transformations.map(matchLiveData) { mi -> mi != null } as MutableLiveData<Boolean?>

    val openStadiumLiveData = MutableLiveData<ViewEvent<String>>()

    val buttonsEnabledLiveData = Transformations.map(matchLiveData) { m -> m is Resource.Success }

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    init {
        loadingInProgressLiveData.addSource(matchLiveData) { loadingInProgressLiveData.value = false }

        fetchMatch()
    }

    private fun buildStadiumPhotoUrl(stadium: Stadium): String {
        return AlfApplication.getProperty("url.image.stadium.background") +
                stadium.id + AlfApplication.getProperty("extension.stadium.background")
    }

    private fun buildTeamLogoUrl(team: Team): String {
        return AlfApplication.getProperty("url.logo.club") + team.club.id + AlfApplication.getProperty("extension.logo.club")
    }

    private fun fetchMatch() {
        loadingInProgressLiveData.value = true
        matchLiveData.value = Resource.Loading()
        matchApiService.getMatchById(
                matchId,
                { matchLiveData.value = Resource.Success(it) },
                { matchLiveData.value = Resource.Error(it) }
        )
    }

    fun getMatch(): Match? {
        return matchLiveData.value?.data
    }

    fun openStadium() {
        val uri: String
        val latitude = stadiumLiveData.value?.latitude
        val longitude = stadiumLiveData.value?.longitude
        if (latitude != null && longitude != null
                && !latitude.equals(0.0) && !longitude.equals(0.0)
        ) {
            uri = String.format(Locale.getDefault(), "geo:$latitude,$longitude?q=$latitude,$longitude")
        } else {
            var address = stadiumLiveData.value?.address
            val name = stadiumLiveData.value?.name
            val city = stadiumLiveData.value?.city
            if (city == null) {
                address = "$city $address"
            }

            uri = String.format(Locale.getDefault(), "geo:0,0?q=$name $address")
        }

        openStadiumLiveData.value = ViewEvent(uri)
    }

}
