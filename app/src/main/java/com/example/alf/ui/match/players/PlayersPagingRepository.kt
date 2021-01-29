package com.example.alf.ui.match.players

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.alf.AlfApplication
import com.example.alf.data.model.Player
import com.example.alf.data.model.Substitution
import com.example.alf.data.paging.AllowableAppearancesPagingSource
import com.example.alf.data.paging.AllowablePlayersForEventPagingSource
import com.example.alf.data.paging.AllowablePlayersForSubstitutionPagingSource
import com.example.alf.service.PlayersService
import kotlinx.coroutines.flow.Flow

class PlayersPagingRepository(private val service: PlayersService) {

    private val networkPageSize = AlfApplication.getProperty("pagination.players.pageSize").toInt()

    fun getSearchResultStreamForAppearance(
            matchId: Int, teamId: Int,
            query: String, sort: String
    ): Flow<PagingData<Player>> {
        return Pager(
            config = PagingConfig(
                pageSize = networkPageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AllowableAppearancesPagingSource(
                        service,
                        matchId, teamId,
                        query, sort
                )
            }
        ).flow
    }

    fun getSearchResultStreamForEvent(
            matchId: Int, teamId: Int, eventTypeId: Int, minute: Int,
            query: String, sort: String
    ): Flow<PagingData<Player>> {
        return Pager(
                config = PagingConfig(
                        pageSize = networkPageSize,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    AllowablePlayersForEventPagingSource(
                            service,
                            matchId, teamId, eventTypeId, minute,
                            query, sort
                    )
                }
        ).flow
    }

    fun getSearchResultStreamForSubstitution(
            matchId: Int, teamId: Int, minute: Int, playerType: Substitution.PlayerType,
            query: String, sort: String
    ): Flow<PagingData<Player>> {
        return Pager(
                config = PagingConfig(
                        pageSize = networkPageSize,
                        enablePlaceholders = false
                ),
                pagingSourceFactory = {
                    AllowablePlayersForSubstitutionPagingSource(
                            service,
                            matchId, teamId, minute, playerType,
                            query, sort
                    )
                }
        ).flow
    }

}