package com.example.alf.ui.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.network.ApiClient
import com.example.alf.network.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonRepository {

    private var apiInterface: ApiInterface? = null

    init {
        apiInterface = ApiClient.getApiClient().create(ApiInterface::class.java)
    }

    fun fetchPersonsByQuery(query: String): LiveData<List<PersonModel>> {
        val data = MutableLiveData<List<PersonModel>>()

        apiInterface?.fetchPersonsPageByQuery(query)?.enqueue(object : Callback<PersonsPageModel> {

            override fun onFailure(call: Call<PersonsPageModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                call: Call<PersonsPageModel>,
                response: Response<PersonsPageModel>
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

    /*fun createPerson(personModel: PersonModel):LiveData<PersonModel>{
        val data = MutableLiveData<PersonModel>()

        apiInterface?.createPerson(personModel)?.enqueue(object : Callback<PersonModel>{
            override fun onFailure(call: Call<PersonModel>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<PersonModel>, response: Response<PersonModel>) {
                val res = response.body()
                if (response.code() == 201 && res!=null){
                    data.value = res
                }else{
                    data.value = null
                }
            }
        })

        return data

    }

    fun deletePerson(id:Int):LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()

        apiInterface?.deletePerson(id)?.enqueue(object : Callback<String>{
            override fun onFailure(call: Call<String>, t: Throwable) {
                data.value = false
            }

            override fun onResponse(call: Call<String>, response: Response<String>) {
                data.value = response.code() == 200
            }
        })

        return data

    }*/

}