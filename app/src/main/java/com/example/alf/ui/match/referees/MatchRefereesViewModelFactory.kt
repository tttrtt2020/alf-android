package com.example.alf.ui.match.referees

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class MatchRefereesViewModelFactory(
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MatchRefereesViewModel(matchId) as T
    }
}