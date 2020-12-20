package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.Formation
import com.example.alf.network.ApiClient
import com.example.alf.network.FormationApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormationApiService {

    private var formationApiInterface: FormationApiInterface = ApiClient.getApiClient().create(
            FormationApiInterface::class.java
    )

    fun fetchAllowableFormations(
            formationsLiveData: MutableLiveData<List<Formation>?>,
            matchId: Int,
            teamId: Int
    ): LiveData<List<Formation>?> {

        formationApiInterface.fetchAllowableFormations(matchId, teamId).enqueue(object : Callback<List<Formation>> {

            override fun onFailure(call: Call<List<Formation>>, t: Throwable) {
                formationsLiveData.value = null
            }

            override fun onResponse(
                call: Call<List<Formation>>,
                response: Response<List<Formation>>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    formationsLiveData.value = res
                } else {
                    formationsLiveData.value = null
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