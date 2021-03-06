package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.AlfApplication
import com.example.alf.data.model.Player
import com.example.alf.service.PlayersService
import retrofit2.HttpException
import java.io.IOException

class AllowablePlayersForEventPagingSource(
        private val service: PlayersService,
        private val matchId: Int,
        private val teamId: Int,
        private val eventTypeId: Int,
        private val minute: Int,
        private val query: String,
        private val sort: String
) : PagingSource<Int, Player>() {

    private val startingPageIndex = AlfApplication.getProperty("pagination.players.startIndex").toInt()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Player> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: startingPageIndex
            val playersPage = service.searchPlayersPageForEvent(
                    matchId, teamId, eventTypeId, minute,
                    query, sort, nextPageNumber
            )
            return LoadResult.Page(
                data = playersPage.content,
                prevKey = if (playersPage.number == startingPageIndex) null else playersPage.number - 1,
                nextKey = if (playersPage.last) null else playersPage.number + 1
            )
        } catch (e: IOException) {
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        }
    }
}