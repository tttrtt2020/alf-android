package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.Formation
import com.example.alf.network.ApiClient
import com.example.alf.network.FormationApiInterface
import com.example.alf.network.Resource
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
            formationsLiveData: MutableLiveData<Resource<List<Formation>>>,
            matchId: Int,
            teamId: Int
    ): LiveData<Resource<List<Formation>>> {

        formationApiInterface.fetchAllowableFormations(matchId, teamId).enqueue(object : Callback<List<Formation>> {

            override fun onFailure(call: Call<List<Formation>>, t: Throwable) {
                formationsLiveData.value = Resource.Error(t.localizedMessage!!, null)
            }

            override fun onResponse(
                call: Call<List<Formation>>,
                response: Response<List<Formation>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    formationsLiveData.value = Resource.Success(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    formationsLiveData.value = Resource.Error(apiError.message, null)
                }
            }
        })

        return formationsLiveData
    }

    fun fetchFormationById(id: Int): LiveData<Formation>? {
        val data = MutableLiveData<Formation>()

        formationApiInterface.fetchFormationById(id).enqueue(object : Callback<Formation> {

            override fun onFailure(call: Call<Formation>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                    call: Call<Formation>,
                    response: Response<Formation>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res
                } else {
                    data.value = null
                }
            }
        })

        return data
    }

    fun setFormation(
            addFormationToMatchLiveData: MutableLiveData<Boolean?>,
            matchId: Int,
            teamId: Int,
            formation: Formation
    ): LiveData<Boolean?> {
        formationApiInterface.setFormation(matchId, teamId, formation).enqueue(object : Callback<Formation> {
            override fun onFailure(call: Call<Formation>, t: Throwable) {
                addFormationToMatchLiveData.value = false
            }

            override fun onResponse(call: Call<Formation>, response: Response<Formation>) {
                addFormationToMatchLiveData.value = (response.code() >= 200 || response.code() <= 299)
            }
        })

        return addFormationToMatchLiveData
    }

}