package com.example.alf.ui.match

import android.app.Application
import androidx.lifecycle.ViewModel

import androidx.lifecycle.ViewModelProvider


class MatchViewModelFactory(private val application: Application, private val id: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MatchViewModel(application, id) as T
    }
}