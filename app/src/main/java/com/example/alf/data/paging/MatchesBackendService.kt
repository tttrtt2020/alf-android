package com.example.alf.data.paging

import com.example.alf.data.model.MatchesPageModel
import com.example.alf.data.repository.MatchRepository

class MatchesBackendService {

    suspend fun searchMatchesPage(nextPageNumber: Int): MatchesPageModel {
        return MatchRepository().fetchMatchesPage(nextPageNumber)
    }

}
