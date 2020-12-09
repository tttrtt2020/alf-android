package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.AlfApplication
import com.example.alf.data.model.Referee
import retrofit2.HttpException
import java.io.IOException

class RefereesPagingSource(
    private val service: RefereesService,
    private var query: String
) : PagingSource<Int, Referee>() {

    private val startingPageIndex = AlfApplication.getProperty("pagination.referees.startIndex").toInt()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Referee> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: startingPageIndex
            val refereesPage = service.searchRefereesPage(query, nextPageNumber)
            return LoadResult.Page(
                data = refereesPage.content,
                prevKey = if (refereesPage.number == startingPageIndex) null else refereesPage.number - 1,
                nextKey = if (refereesPage.last) null else refereesPage.number + 1
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