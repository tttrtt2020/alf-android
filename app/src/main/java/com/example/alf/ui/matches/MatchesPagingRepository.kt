package com.example.alf.ui.matches

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import com.example.alf.data.paging.MatchesPagingSource
import com.example.alf.data.paging.MatchesService
import kotlinx.coroutines.flow.Flow

class MatchesPagingRepository(private val service: MatchesService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.matches.pageSize").toInt()

    fun getSearchResultStream(query: String): Flow<PagingData<Match>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { MatchesPagingSource(service, query) }
        ).flow
    }

}