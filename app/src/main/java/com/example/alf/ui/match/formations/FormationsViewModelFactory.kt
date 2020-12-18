package com.example.alf.ui.match.formations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class FormationsViewModelFactory(
        private val matchId: Int,
        private val teamId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FormationSelectionViewModel(matchId, teamId) as T
    }
}