package com.example.alf.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.Person
import com.example.alf.data.model.PersonsPage
import com.example.alf.network.ApiClient
import com.example.alf.network.PersonApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonApiService {

    private var personApiInterface: PersonApiInterface = ApiClient.getApiClient().create(PersonApiInterface::class.java)

    /*fun fetchPersonsByQuery(query: String): LiveData<List<Person>> {
        val data = MutableLiveData<List<Person>>()

        apiInterface?.fetchPersonsPageByQuery(query)?.enqueue(object : Callback<PersonsPage> {

            override fun onFailure(call: Call<PersonsPage>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(
                    call: Call<PersonsPage>,
                    response: Response<PersonsPage>
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

    }*/

    fun getPersonById(personLiveData: MutableLiveData<Person>, id: Int): LiveData<Person>? {

        personApiInterface.fetchPersonById(id).enqueue(object : Callback<Person> {

            override fun onFailure(call: Call<Person>, t: Throwable) {
                personLiveData.value = null
            }

            override fun onResponse(
                call: Call<Person>,
                response: Response<Person>
            ) {
                val res = response.body()
                if (response.code() == 200 && res != null) {
                    personLiveData.value = res
                } else {
                    personLiveData.value = null
                }
            }
        })

        return personLiveData
    }

    suspend fun fetchPersonsPage(query: String, sort: String, nextPageNumber: Int): PersonsPage {
        return personApiInterface.fetchPersonsPage(query, sort, nextPageNumber)
    }

    fun createPerson(personLiveData: MutableLiveData<Person>, person: Person): LiveData<Person>{

        personApiInterface.createPerson(person).enqueue(object : Callback<Person>{
            override fun onFailure(call: Call<Person>, t: Throwable) {
                personLiveData.value = null
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val res = response.body()
                if (response.code() == 201 && res != null) {
                    personLiveData.value = res
                } else {
                    personLiveData.value = null
                }
            }
        })

        return personLiveData
    }

    fun updatePerson(resultLiveData: MutableLiveData<Boolean?>, person: Person): LiveData<Boolean?>{

        personApiInterface.updatePerson(person.id, person).enqueue(object : Callback<Person>{
            override fun onFailure(call: Call<Person>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                resultLiveData.value = (response.code() >= 200 || response.code() <= 299)
            }
        })

        return resultLiveData
    }

    fun deletePerson(resultLiveData: MutableLiveData<Boolean?>, person: Person): LiveData<Boolean?> {

        personApiInterface.deletePerson(person.id).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                resultLiveData.value = false
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                resultLiveData.value = (response.code() == 200 || response.code() == 204)
            }
        })

        return resultLiveData
    }

}