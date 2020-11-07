package com.example.alf.data.paging

import com.example.alf.data.model.PersonsPageModel
import com.example.alf.data.repository.PersonApiService

class PersonsService {

    suspend fun searchPersonsPage(query: String, nextPageNumber: Int): PersonsPageModel {
        return PersonApiService().fetchPersonsPage(query, nextPageNumber)
    }

}
