package com.example.alf.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.paging.MatchesBackendService
import com.example.alf.data.paging.MatchesPagingSource
import com.example.alf.data.repository.MatchApiService

class MatchesViewModel(application: Application) : AndroidViewModel(application) {

    private val pageSize = AlfApplication.getProperty("pagination.matches.pageSize").toInt()

    private var matchApiService: MatchApiService? = null
    var matchListLiveData: LiveData<List<Match>>? = null
    /*var createMatchLiveData: LiveData<MatchModel>? = null
    var deleteMatchLiveData: LiveData<Boolean>? = null*/

    val flow = Pager(config = PagingConfig(
        pageSize = pageSize
    )) {
        MatchesPagingSource(MatchesBackendService())
    }.flow.cachedIn(viewModelScope)

    init {
        matchApiService = MatchApiService()
        matchListLiveData = MutableLiveData()
        /*createMatchLiveData = MutableLiveData()
        deleteMatchLiveData = MutableLiveData()*/
    }

    /*fun fetchAllMatches() {
        matchModelListLiveData = matchRepository?.fetchAllMatches()
    }*/

    fun fetchAllMatches() {
        matchListLiveData = matchApiService?.fetchMatches()
    }

    /*fun createMatch(matchModel: MatchModel) {
        createMatchLiveData = matchRepository?.createMatch(matchModel)
    }

    fun deleteMatch(id: Int) {
        deleteMatchLiveData = matchRepository?.deleteMatch(id)
    }*/

}