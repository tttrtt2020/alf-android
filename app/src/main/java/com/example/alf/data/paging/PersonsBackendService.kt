package com.example.alf.data.paging

import com.example.alf.data.model.PersonsPageModel
import com.example.alf.data.repository.PersonRepository

class PersonsBackendService {

    suspend fun searchPersonsPage(query: String, nextPageNumber: Int): PersonsPageModel {
        return PersonRepository().fetchPersonsPage(query, nextPageNumber)
    }

}
