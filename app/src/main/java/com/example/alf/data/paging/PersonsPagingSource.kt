package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.data.model.PersonModel
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class PersonsPagingSource(
    private val service: PersonsService,
    private var query: String
) : PagingSource<Int, PersonModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonModel> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: STARTING_PAGE_INDEX
            val personsPageModel = service.searchPersonsPage(query, nextPageNumber)
            return LoadResult.Page(
                data = personsPageModel.content,
                prevKey = if (personsPageModel.number == STARTING_PAGE_INDEX) null else personsPageModel.number - 1,
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