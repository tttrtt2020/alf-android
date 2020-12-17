package com.example.alf.ui.match.fieldPositions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FieldPositionsViewModelFactory(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FieldPositionsViewModel(matchId, teamId) as T
    }
}