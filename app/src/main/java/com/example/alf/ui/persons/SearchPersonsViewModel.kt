package com.example.alf.ui.persons

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.Person
import com.example.alf.data.repository.PersonApiService
import kotlinx.coroutines.flow.Flow


class SearchPersonsViewModel(
    private val personsPagingRepository: PersonsPagingRepository
) : ViewModel() {

    private var personApiService: PersonApiService? = null
    /*var createPersonLiveData: LiveData<Person>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    private var currentQueryValue: String? = null

    var currentSearchResult: Flow<PagingData<Person>>? = null

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()

    init {
        personApiService = PersonApiService()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/

        loadingInProgressLiveData.apply {
            //addSource(currentSearchResult!!.asLiveData()) { loadingInProgressLiveData.value = false }
            //addSource(personListLiveData) { loadingInProgressLiveData.value = false }
        }
    }

    fun searchPersons(query: String, sort: String): Flow<PagingData<Person>> {
        val lastResult = currentSearchResult
        if (query == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = query
        val newResult: Flow<PagingData<Person>> = personsPagingRepository
            .getSearchResultStream(query, sort)
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
