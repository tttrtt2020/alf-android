package com.example.alf.ui.match.players

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Player
import com.example.alf.data.paging.AllowableMatchPlayersPagingSource
import com.example.alf.service.PlayersService
import kotlinx.coroutines.flow.Flow

class PlayersPagingRepository(private val service: PlayersService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.players.pageSize").toInt()

    fun getSearchResultStream(matchId: Int, teamId: Int, query: String, sort: String): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AllowableMatchPlayersPagingSource(service, matchId, teamId, query, sort)
            }
        ).flow
    }

}