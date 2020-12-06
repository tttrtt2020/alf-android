package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.FormationsPageModel
import com.example.alf.network.ApiClient
import com.example.alf.network.FormationApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FormationApiService {

    private var formationApiInterface: FormationApiInterface = ApiClient.getApiClient().create(FormationApiInterface::class.java)

    fun fetchFormations(): LiveData<List<Formation>> {
        val data = MutableLiveData<List<Formation>>()

        formationApiInterface.fetchFormationsPage().enqueue(object : Callback<FormationsPageModel> {

            override fun onFailure(call: Call<FormationsPageModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<FormationsPageModel>,
                response: Response<FormationsPageModel>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    data.value = res.content
                } else {
                    data.value = null
                }
            }
        })

        return data
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
}