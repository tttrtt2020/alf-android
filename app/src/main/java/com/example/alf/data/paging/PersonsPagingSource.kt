package com.example.alf.data.paging

import androidx.paging.PagingSource
import com.example.alf.data.model.PersonModel
import retrofit2.HttpException
import java.io.IOException

class PersonsPagingSource(
    private val backend: PersonsBackendService,
    private var query: String
) : PagingSource<Int, PersonModel>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PersonModel> {
        try {
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = backend.searchPersonsPage(query, nextPageNumber)
            return LoadResult.Page(
                data = response.content,
                prevKey = null, // Only paging forward.
                nextKey = response.number + 1
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