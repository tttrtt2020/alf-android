package com.example.alf.data.repository

import com.example.alf.data.model.Person
import com.example.alf.data.model.PersonsPage
import com.example.alf.network.ApiClient
import com.example.alf.network.PersonApiInterface
import com.example.alf.network.errorHandling.ApiError
import com.example.alf.network.errorHandling.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PersonApiService {

    private var personApiInterface: PersonApiInterface = ApiClient.getApiClient().create(PersonApiInterface::class.java)

    fun getPersonById(
            id: Int,
            successCallback: (person: Person) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        personApiInterface.fetchPersonById(id).enqueue(object : Callback<Person> {

            override fun onFailure(call: Call<Person>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(
                call: Call<Person>,
                response: Response<Person>
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

    suspend fun fetchPersonsPage(query: String, sort: String, nextPageNumber: Int): PersonsPage {
        return personApiInterface.fetchPersonsPage(query, sort, nextPageNumber)
    }

    fun createPerson(
            person: Person,
            successCallback: (person: Person) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        personApiInterface.createPerson(person).enqueue(object : Callback<Person> {

            override fun onFailure(call: Call<Person>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.code() == 201) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun updatePerson(
            person: Person,
            successCallback: (person: Person) -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        personApiInterface.updatePerson(person.id, person).enqueue(object : Callback<Person> {

            override fun onFailure(call: Call<Person>, t: Throwable) {
                failureCallback(t.localizedMessage!!)
            }

            override fun onResponse(call: Call<Person>, response: Response<Person>) {
                if (response.isSuccessful) {
                    successCallback(response.body()!!)
                } else {
                    val apiError: ApiError = ErrorUtils.parseError(response)
                    failureCallback(apiError.message)
                }
            }

        })
    }

    fun deletePerson(
            person: Person,
            successCallback: () -> Unit,
            failureCallback: (errorMessage: String) -> Unit
    ) {
        personApiInterface.deletePerson(person.id).enqueue(object : Callback<Unit> {

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

}