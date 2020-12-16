package com.example.alf.data.paging

import com.example.alf.data.model.RefereesPage
import com.example.alf.data.repository.RefereeApiService

class RefereesService {

    suspend fun searchRefereesPage(
            query: String,
            sort: String,
            nextPageNumber: Int
    ): RefereesPage {
        return RefereeApiService().fetchRefereesPage(query, sort, nextPageNumber)
    }

}
