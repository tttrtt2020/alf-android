package com.example.alf.ui.match.referees

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class MatchRefereesViewModelFactory(
        private val application: Application,
        private val matchId: Int
        ) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MatchRefereesViewModel(application, matchId) as T
    }
}