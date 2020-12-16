package com.example.alf.data.paging

import com.example.alf.data.model.MatchesPage
import com.example.alf.data.repository.MatchApiService

class MatchesService {

    suspend fun searchMatchesPage(
            query: String,
            sort: String,
            nextPageNumber: Int
    ): MatchesPage {
        return MatchApiService().fetchMatchesPage(query, sort, nextPageNumber)
    }

}
