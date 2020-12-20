package com.example.alf.ui.match.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TeamViewModelFactory(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamViewModel(matchId, teamId) as T
    }
}