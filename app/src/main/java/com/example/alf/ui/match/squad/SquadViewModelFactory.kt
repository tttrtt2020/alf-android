package com.example.alf.ui.match.squad

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class SquadViewModelFactory(
        private val application: Application,
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SquadViewModel(application, matchId, teamId) as T
    }
}