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

    private val startingPageIndex = AlfApplication.getProperty("pagination.persons.startIndex").toInt()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Person> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: startingPageIndex
            val personsPage = service.searchPersonsPage(query, nextPageNumber)
            return LoadResult.Page(
                data = personsPage.content,
                prevKey = if (personsPage.number == startingPageIndex) null else personsPage.number - 1,
                nextKey = if (personsPage.last) null else personsPage.number + 1
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