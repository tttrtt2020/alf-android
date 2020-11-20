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

    }*/

    fun fetchPersonById(personLiveData: MutableLiveData<Person>, id: Int): LiveData<Person>? {

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

    suspend fun fetchPersonsPage(query: String, nextPageNumber: Int): PersonsPage {
        return personApiInterface.fetchPersonsPage(query, nextPageNumber)
    }

    fun createPerson(person: Person): LiveData<Person>{
        val data = MutableLiveData<Person>()

        personApiInterface.createPerson(person).enqueue(object : Callback<Person>{
            override fun onFailure(call: Call<Person>, t: Throwable) {
                data.value = null
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val res = response.body()
                if (response.code() == 201 && res!=null){
                    data.value = res
                } else {
                    data.value = null
                }
            }
        })

        return data
    }

    fun updatePerson(personLiveData: MutableLiveData<Person>, person: Person?): LiveData<Person>{

        if (person == null) {
            personLiveData.value = null
            return personLiveData
        }

        // todo: think if response is needed and on failure state

        personApiInterface.updatePerson(person.id, person).enqueue(object : Callback<Person>{
            override fun onFailure(call: Call<Person>, t: Throwable) {
                personLiveData.value = null
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                val res = response.body()
                if (response.code() == 200 && res!=null){
                    personLiveData.value = res
                } else {
                    personLiveData.value = null
                }
            }
        })

        return personLiveData
    }

    /*fun deletePerson(id:Int):LiveData<Boolean>{
        val data = MutableLiveData<Boolean>()

        personApiInterface.deletePerson(id)?.enqueue(object : Callback<String>{
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