package com.example.alf.ui.match.statuses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class StatusesViewModelFactory(
        private val matchId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return StatusSelectionViewModel(matchId) as T
    }
}