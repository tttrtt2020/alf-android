package com.example.alf.ui.match

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.alf.data.model.match.MatchInfo
import com.example.alf.data.repository.MatchApiService

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService = MatchApiService()
    var matchLiveData: MutableLiveData<MatchInfo> = MutableLiveData()
    var hostTeamNameLiveData: LiveData<String> = Transformations.map(matchLiveData) { m -> m.mainInfo.match.hostTeam.name }

    init {

    }

    fun getMatchById(id: Int) {
        matchApiService.getMatchInfoById(matchLiveData, id)
    }

}
