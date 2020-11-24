package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.AlfApplication
import com.example.alf.data.model.Match
import retrofit2.HttpException
import java.io.IOException

class MatchesPagingSource(
    private val backend: MatchesBackendService,
) : PagingSource<Int, Match>() {

    private val startingPageIndex = AlfApplication.getProperty("pagination.matches.startIndex").toInt()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Match> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: startingPageIndex
            val matchesPage = backend.searchMatchesPage(nextPageNumber)
            return LoadResult.Page(
                data = matchesPage.content,
                prevKey = if (matchesPage.number == startingPageIndex) null else matchesPage.number - 1,
                nextKey = if (matchesPage.last) null else matchesPage.number + 1
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