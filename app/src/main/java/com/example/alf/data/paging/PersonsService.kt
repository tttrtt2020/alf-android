package com.example.alf.data.paging

import com.example.alf.data.model.PersonsPage
import com.example.alf.data.repository.PersonApiService

class PersonsService {

    suspend fun searchPersonsPage(query: String, nextPageNumber: Int): PersonsPage {
        return PersonApiService().fetchPersonsPage(query, nextPageNumber)
    }

}
