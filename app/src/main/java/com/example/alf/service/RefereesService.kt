package com.example.alf.service

import com.example.alf.data.model.RefereesPage
import com.example.alf.data.repository.RefereeApiService

class RefereesService {

    suspend fun searchMatchRefereesPage(
            matchId: Int,
            query: String,
            sort: String,
            nextPageNumber: Int
    ): RefereesPage {
        return RefereeApiService().fetchAllowableMatchRefereesPage(
                matchId, query, sort, nextPageNumber
        )
    }

}
