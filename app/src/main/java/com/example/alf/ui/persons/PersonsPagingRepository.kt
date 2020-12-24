package com.example.alf.ui.persons

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Person
import com.example.alf.data.paging.PersonsPagingSource
import com.example.alf.service.PersonsService
import kotlinx.coroutines.flow.Flow

class PersonsPagingRepository(private val service: PersonsService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.persons.pageSize").toInt()

    fun getSearchResultStream(query: String, sort: String): Flow<PagingData<Person>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonsPagingSource(service, query, sort) }
        ).flow
    }

}