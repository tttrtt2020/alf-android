package com.example.alf.ui.match.squads

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.Squads
import com.example.alf.data.repository.MatchApiService

class SquadsViewModel(application: Application) : AndroidViewModel(application) {

    private var matchApiService: MatchApiService? = null
    var matchPersonsModelLiveData: LiveData<Squads>? = null

    init {
        matchApiService = MatchApiService()
        matchPersonsModelLiveData = MutableLiveData()
    }

    fun fetchMatchSquadsInfoById(id: Int) {
        matchPersonsModelLiveData = matchApiService?.fetchMatchSquadsInfoById(id)
    }

}
