package com.example.alf.ui.match

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.MatchModel
import com.example.alf.data.repository.MatchRepository

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private var matchRepository: MatchRepository? = null
    var matchModelLiveData: LiveData<MatchModel>? = null
    /*var createMatchLiveData: LiveData<MatchModel>? = null
    var deleteMatchLiveData: LiveData<Boolean>? = null*/

    init {
        matchRepository = MatchRepository()
        matchModelLiveData = MutableLiveData()
        /*createMatchLiveData = MutableLiveData()
        deleteMatchLiveData = MutableLiveData()*/
    }

    fun fetchMatchById(id: Int) {
        matchModelLiveData = matchRepository?.fetchMatchById(id)
    }

    /*fun createMatch(matchModel: MatchModel) {
        createMatchLiveData = matchRepository?.createMatch(matchModel)
    }

    fun deleteMatch(id: Int) {
        deleteMatchLiveData = matchRepository?.deleteMatch(id)
    }*/

}
