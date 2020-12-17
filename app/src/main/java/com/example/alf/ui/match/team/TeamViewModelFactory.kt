package com.example.alf.ui.match.team

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class TeamViewModelFactory(
        private val application: Application,
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamViewModel(application, matchId, teamId) as T
    }
}