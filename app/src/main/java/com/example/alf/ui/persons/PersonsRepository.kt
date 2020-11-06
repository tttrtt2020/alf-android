package com.example.alf.ui.persons

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.data.model.PersonModel
import com.example.alf.data.paging.PersonsPagingSource
import com.example.alf.data.paging.PersonsService
import kotlinx.coroutines.flow.Flow

class PersonsRepository(private val service: PersonsService) {

    fun getSearchResultStream(query: String): Flow<PagingData<PersonModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PersonsPagingSource(service, query) }
        ).flow
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }

}