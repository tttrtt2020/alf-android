package com.example.alf.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.alf.data.model.MatchModel
import com.example.alf.data.paging.MatchesBackendService
import com.example.alf.data.paging.MatchesPagingSource
import com.example.alf.data.repository.MatchRepository

class MatchesViewModel(application: Application) : AndroidViewModel(application) {

    private var matchRepository: MatchRepository? = null
    var matchModelListLiveData: LiveData<List<MatchModel>>? = null
    /*var createMatchLiveData: LiveData<MatchModel>? = null
    var deleteMatchLiveData: LiveData<Boolean>? = null*/

    val flow = Pager(PagingConfig(pageSize = 20)) {
        MatchesPagingSource(MatchesBackendService())
    }.flow.cachedIn(viewModelScope)

    init {
        matchRepository = MatchRepository()
        matchModelListLiveData = MutableLiveData()
        /*createMatchLiveData = MutableLiveData()
        deleteMatchLiveData = MutableLiveData()*/
    }

    /*fun fetchAllMatches() {
        matchModelListLiveData = matchRepository?.fetchAllMatches()
    }*/

    fun fetchAllMatches() {
        matchModelListLiveData = matchRepository?.fetchMatches()
    }

    /*fun createMatch(matchModel: MatchModel) {
        createMatchLiveData = matchRepository?.createMatch(matchModel)
    }

    fun deleteMatch(id: Int) {
        deleteMatchLiveData = matchRepository?.deleteMatch(id)
    }*/

}