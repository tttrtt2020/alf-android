package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.FieldPosition
import com.example.alf.network.ApiClient
import com.example.alf.network.FieldPositionApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FieldPositionApiService {

    private var fieldPositionApiInterface: FieldPositionApiInterface = ApiClient.getApiClient().create(
            FieldPositionApiInterface::class.java
    )

    fun fetchFreeFieldPositions(fieldPositionsLiveData: MutableLiveData<List<FieldPosition>>, matchId: Int, teamId: Int): LiveData<List<FieldPosition>> {

        fieldPositionApiInterface.fetchFreeFieldPositions(matchId, teamId).enqueue(object : Callback<List<FieldPosition>> {

            override fun onFailure(call: Call<List<FieldPosition>>, t: Throwable) {
                fieldPositionsLiveData.value = null
            }

            override fun onResponse(
                    call: Call<List<FieldPosition>>,
                    response: Response<List<FieldPosition>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    fieldPositionsLiveData.value = res
                } else {
                    fieldPositionsLiveData.value = null
                }
            }
        })

        return fieldPositionsLiveData
    }
}