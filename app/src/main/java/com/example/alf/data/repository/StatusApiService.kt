package com.example.alf.data.repository

import com.example.alf.data.model.match.Status
import com.example.alf.network.ApiClient
import com.example.alf.network.StatusApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StatusApiService {

    private var statusApiInterface: StatusApiInterface = ApiClient.getApiClient().create(
            StatusApiInterface::class.java
    )

    fun fetchAllowableStatuses(
            matchId: Int,
            successCallback: (statuses: List<Status>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        statusApiInterface.fetchAllowableStatuses(matchId).enqueue(object : Callback<List<Status>> {

            override fun onFailure(call: Call<List<Status>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<List<Status>>,
                response: Response<List<Status>>
            ) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun setStatus(
            matchId: Int,
            status: Status,
            successCallback: (status: Status) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        statusApiInterface.setStatus(matchId, status).enqueue(object : Callback<Status> {

            override fun onFailure(call: Call<Status>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Status>, response: Response<Status>) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}