package com.example.alf.ui.matches

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.MatchListItem
import com.example.alf.data.paging.MatchesPagingSource
import com.example.alf.service.MatchesService
import kotlinx.coroutines.flow.Flow

class MatchesPagingRepository(private val service: MatchesService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.matches.pageSize").toInt()

    fun getSearchResultStream(query: String, sort: String): Flow<PagingData<MatchListItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MatchesPagingSource(service, query, sort) }
        ).flow
    }

}