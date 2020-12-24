package com.example.alf.ui.match.referees

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Referee
import com.example.alf.data.paging.AllowableMatchRefereesPagingSource
import com.example.alf.service.RefereesService
import kotlinx.coroutines.flow.Flow

class RefereesPagingRepository(private val service: RefereesService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.referees.pageSize").toInt()

    fun getSearchResultStream(matchId: Int, query: String, sort: String): Flow<PagingData<Referee>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AllowableMatchRefereesPagingSource(service, matchId, query, sort)
            }
        ).flow
    }

}