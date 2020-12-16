package com.example.alf.ui.match.players

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Player
import com.example.alf.data.paging.PlayersPagingSource
import com.example.alf.data.paging.PlayersService
import kotlinx.coroutines.flow.Flow

class PlayersPagingRepository(private val service: PlayersService, val matchId: Int, val teamId: Int) {

    private val networkPageSize = AlfApplication.getProperty("pagination.players.pageSize").toInt()

    fun getSearchResultStream(query: String, sort: String): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayersPagingSource(service, matchId, teamId, query, sort) }
        ).flow
    }

    fun getSearchResultPager(query: String, sort: String): Pager<Int, Player> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PlayersPagingSource(service, matchId, teamId, query, sort) }
        )
    }

}