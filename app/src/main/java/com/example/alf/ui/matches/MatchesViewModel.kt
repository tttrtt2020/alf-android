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

    val flow = Pager(
            config = PagingConfig(
                    pageSize = pageSize
            )
    ) {
        MatchesPagingSource(MatchesBackendService())
    }.flow.cachedIn(viewModelScope)

    // todo: rework to MediatorLiveData depending on flow or similar
    var loadingInProgressLiveData: MutableLiveData<Boolean> = MutableLiveData<Boolean>(true)

    init {
        matchApiService = MatchApiService()
        matchListLiveData = MutableLiveData()
    }

    /*fun fetchAllMatches() {
        matchListLiveData = matchRepository?.fetchAllMatches()
    }*/

    fun fetchAllMatches() {
        matchListLiveData = matchApiService?.fetchMatches()
    }

}