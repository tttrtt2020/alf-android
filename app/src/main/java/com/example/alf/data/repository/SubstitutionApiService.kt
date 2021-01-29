package com.example.alf.data.repository

import com.example.alf.data.model.Substitution
import com.example.alf.network.ApiClient
import com.example.alf.network.SubstitutionApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SubstitutionApiService {

    private var substitutionApiInterface: SubstitutionApiInterface = ApiClient.getApiClient().create(SubstitutionApiInterface::class.java)

    fun createMatchSubstitution(
            matchId: Int,
            substitution: Substitution,
            successCallback: (substitution: Substitution) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        substitutionApiInterface.createMatchSubstitution(matchId, substitution).enqueue(object : Callback<Substitution> {

            override fun onFailure(call: Call<Substitution>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Substitution>, response: Response<Substitution>) {
                if (response.code() == 201) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun deleteMatchSubstitution(
            matchId: Int,
            substitutionId: Int,
            successCallback: () -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        substitutionApiInterface.deleteMatchSubstitution(matchId, substitutionId).enqueue(object : Callback<Unit> {

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.code() == 200 || response.code() == 204) {
                    successCallback()
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun fetchMatchSubstitutions(
            matchId: Int,
            sort: String,
            sort2: String,
            successCallback: (substitutions: List<Substitution>) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        substitutionApiInterface.fetchMatchSubstitutions(matchId, sort, sort2).enqueue(object : Callback<List<Substitution>> {

            override fun onFailure(call: Call<List<Substitution>>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                    call: Call<List<Substitution>>,
                    response: Response<List<Substitution>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    successCallback(res)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

}