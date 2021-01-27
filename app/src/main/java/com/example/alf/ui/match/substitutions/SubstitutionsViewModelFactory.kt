package com.example.alf.ui.match.substitutions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class SubstitutionsViewModelFactory(
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SubstitutionsViewModel(matchId) as T
    }
}