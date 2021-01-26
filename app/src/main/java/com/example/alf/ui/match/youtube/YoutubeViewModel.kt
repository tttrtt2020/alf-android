package com.example.alf.ui.match.youtube

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alf.data.model.Match
import com.example.alf.data.model.match.YoutubeUrl
import com.example.alf.data.repository.MatchApiService
import com.example.alf.ui.common.ViewEvent

class YoutubeViewModel(
        private val match: Match,
) : ViewModel() {

    private val matchApiService = MatchApiService()

    private val setYoutubeIdActionLiveData = MutableLiveData<ViewEvent<Boolean>>()

    val youtubeIdLiveData = MutableLiveData(YoutubeUrl("", if (match.youtubeId == null) "" else match.youtubeId!!))

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()

    val message = MutableLiveData<ViewEvent<String>>()
    val goBack = MutableLiveData<ViewEvent<Unit>>()

    init {
        loadingInProgressLiveData.addSource(setYoutubeIdActionLiveData) { loadingInProgressLiveData.value = false }
    }

    fun setYoutubeId() {
        loadingInProgressLiveData.value = true
        matchApiService.setYoutubeId(
                match.id,
                youtubeIdLiveData.value!!,
                {
                    match.youtubeId = youtubeIdLiveData.value!!.id
                    setYoutubeIdActionLiveData.value = ViewEvent(true)
                    goBack.value = ViewEvent(Unit)
                },
                {
                    setYoutubeIdActionLiveData.value = ViewEvent(false)
                    message.value = ViewEvent(it)
                }
        )
    }

}
