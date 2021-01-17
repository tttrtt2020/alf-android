package com.example.alf.data.repository

import com.example.alf.data.model.match.FieldPosition
import com.example.alf.network.ApiClient
import com.example.alf.network.FieldPositionApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FieldPositionApiService {

    private var fieldPositionApiInterface: FieldPositionApiInterface = ApiClient.getApiClient().create(
            FieldPositionApiInterface::class.java
    )

    fun fetchFreeFieldPositions(
            matchId: Int,
            teamId: Int,
            successCallback: (fieldPositions: List<FieldPosition>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        fieldPositionApiInterface.fetchFreeFieldPositions(matchId, teamId).enqueue(object : Callback<List<FieldPosition>> {

            override fun onFailure(call: Call<List<FieldPosition>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<FieldPosition>>,
                    response: Response<List<FieldPosition>>
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
}