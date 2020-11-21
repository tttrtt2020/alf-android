package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.AlfApplication
import com.example.alf.data.model.Person
import retrofit2.HttpException
import java.io.IOException

class PersonsPagingSource(
    private val service: PersonsService,
    private var query: String
) : PagingSource<Int, Person>() {

    private val startingPageIndex = AlfApplication.getProperty("pagination.startIndex").toInt()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: startingPageIndex
            val personsPageModel = service.searchPersonsPage(query, nextPageNumber)
            return LoadResult.Page(
                data = personsPageModel.content,
                prevKey = if (personsPageModel.number == startingPageIndex) null else personsPageModel.number - 1,
                nextKey = if (personsPageModel.last) null else personsPageModel.number + 1
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