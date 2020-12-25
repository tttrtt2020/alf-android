package com.example.alf.ui.match.team.selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class TeamSelectionViewModelFactory(
        private val matchId: Int,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TeamSelectionViewModel(matchId) as T
    }
}