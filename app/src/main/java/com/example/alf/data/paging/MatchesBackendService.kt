package com.example.alf.data.paging

import com.example.alf.data.model.MatchesPageModel
import com.example.alf.data.repository.MatchApiService

class MatchesBackendService {

    suspend fun searchMatchesPage(nextPageNumber: Int): MatchesPageModel {
        return MatchApiService().fetchMatchesPage(nextPageNumber)
    }

}
