package com.example.alf.ui.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Person
import com.example.alf.data.repository.PersonApiService
import kotlinx.coroutines.flow.Flow


class SearchPersonsViewModel(
    private val personsPagingRepository: PersonsPagingRepository
) : ViewModel() {

    private var personApiService: PersonApiService? = null
    var personListLiveData: LiveData<List<Person>>? = null
    /*var createPersonLiveData: LiveData<Person>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<Person>>? = null

    init {
        personApiService = PersonApiService()
        personListLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/
    }

    fun searchPersons(queryString: String): Flow<PagingData<Person>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<Person>> = personsPagingRepository
            .getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    /*fun fetchAllPersons() {
        personListLiveData = personRepository?.fetchAllPersons()
    }*/

    /*fun fetchPersonsByQuery(query: String) {
        personListLiveData = personRepository?.fetchPersonsByQuery(query)
    }*/

    /*fun createPerson(person: Person) {
        createPersonLiveData = personRepository?.createPerson(person)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

}
