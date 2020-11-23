package com.example.alf.ui.match.squads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.SquadsModel
import com.example.alf.data.repository.MatchApiService

class SquadsViewModel(application: Application) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService? = null
    var matchPersonsModelLiveData: LiveData<SquadsModel>? = null
    /*var createMatchLiveData: LiveData<MatchModel>? = null
    var deleteMatchLiveData: LiveData<Boolean>? = null*/

    init {
        matchApiService = MatchApiService()
        matchPersonsModelLiveData = MutableLiveData()
        /*createMatchLiveData = MutableLiveData()
        deleteMatchLiveData = MutableLiveData()*/
    }

    fun fetchMatchSquadsInfoById(id: Int) {
        matchPersonsModelLiveData = matchApiService?.fetchMatchSquadsInfoById(id)
    }

    /*fun createMatch(matchModel: MatchModel) {
        createMatchLiveData = matchRepository?.createMatch(matchModel)
    }

    fun deleteMatch(id: Int) {
        deleteMatchLiveData = matchRepository?.deleteMatch(id)
    }*/

}
