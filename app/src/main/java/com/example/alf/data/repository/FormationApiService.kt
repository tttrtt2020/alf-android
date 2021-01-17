package com.example.alf.data.repository

import com.example.alf.data.model.match.Formation
import com.example.alf.network.ApiClient
import com.example.alf.network.FormationApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormationApiService {

    private var formationApiInterface: FormationApiInterface = ApiClient.getApiClient().create(
            FormationApiInterface::class.java
    )

    fun fetchAllowableFormations(
            matchId: Int,
            teamId: Int,
            successCallback: (formations: List<Formation>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        formationApiInterface.fetchAllowableFormations(matchId, teamId).enqueue(object : Callback<List<Formation>> {

            override fun onFailure(call: Call<List<Formation>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<List<Formation>>,
                response: Response<List<Formation>>
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

    fun setFormation(
            matchId: Int,
            teamId: Int,
            formation: Formation,
            successCallback: (formation: Formation) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        formationApiInterface.setFormation(matchId, teamId, formation).enqueue(object : Callback<Formation> {

            override fun onFailure(call: Call<Formation>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Formation>, response: Response<Formation>) {
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