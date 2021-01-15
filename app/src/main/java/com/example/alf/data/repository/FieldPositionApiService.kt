package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.network.ApiClient
import com.example.alf.network.FieldPositionApiInterface
import com.example.alf.network.Resource
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
            fieldPositionsLiveData: MutableLiveData<Resource<List<FieldPosition>>>,
            matchId: Int,
            teamId: Int
    ): LiveData<Resource<List<FieldPosition>>> {

        fieldPositionApiInterface.fetchFreeFieldPositions(matchId, teamId).enqueue(object : Callback<List<FieldPosition>> {

            override fun onFailure(call: Call<List<FieldPosition>>, t: Throwable) {
                fieldPositionsLiveData.value = Resource.Error(t.localizedMessage!!, null)
            }

            override fun onResponse(
                    call: Call<List<FieldPosition>>,
                    response: Response<List<FieldPosition>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    fieldPositionsLiveData.value = Resource.Success(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    fieldPositionsLiveData.value = Resource.Error(apiError.message, null)
                }
            }
        })

        return fieldPositionsLiveData
    }
}