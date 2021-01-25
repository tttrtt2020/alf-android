package com.example.alf.ui.match.youtube

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.alf.data.model.Match


class YoutubeViewModelFactory(
        private val match: Match
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return YoutubeViewModel(match) as T
    }
}