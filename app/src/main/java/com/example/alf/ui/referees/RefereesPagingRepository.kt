package com.example.alf.ui.referees

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Referee
import com.example.alf.data.paging.RefereesPagingSource
import com.example.alf.data.paging.RefereesService
import kotlinx.coroutines.flow.Flow

class RefereesPagingRepository(private val service: RefereesService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.referees.pageSize").toInt()

    fun getSearchResultStream(query: String): Flow<PagingData<Referee>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RefereesPagingSource(service, query) }
        ).flow
    }

    fun getSearchResultPager(query: String): Pager<Int, Referee> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { RefereesPagingSource(service, query) }
        )
    }

}