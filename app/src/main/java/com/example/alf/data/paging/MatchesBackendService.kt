package com.example.alf.data.paging

import com.example.alf.data.model.MatchesPage
import com.example.alf.data.repository.MatchApiService

class MatchesBackendService {

    suspend fun searchMatchesPage(nextPageNumber: Int): MatchesPage {
        return MatchApiService().fetchMatchesPage(nextPageNumber)
    }

}
