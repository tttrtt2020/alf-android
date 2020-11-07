package com.example.alf.ui.persons

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.alf.data.model.PersonModel
import com.example.alf.data.repository.PersonApiService
import kotlinx.coroutines.flow.Flow


class SearchPersonsViewModel(
    private val personsPagingRepository: PersonsPagingRepository
) : ViewModel() {

    private var personApiService: PersonApiService? = null
    var personModelListLiveData: LiveData<List<PersonModel>>? = null
    /*var createPersonLiveData: LiveData<PersonModel>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    private var currentQueryValue: String? = null

    private var currentSearchResult: Flow<PagingData<PersonModel>>? = null

    init {
        personApiService = PersonApiService()
        personModelListLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/
    }

    fun searchPersons(queryString: String): Flow<PagingData<PersonModel>> {
        val lastResult = currentSearchResult
        if (queryString == currentQueryValue && lastResult != null) {
            return lastResult
        }
        currentQueryValue = queryString
        val newResult: Flow<PagingData<PersonModel>> = personsPagingRepository
            .getSearchResultStream(queryString)
            .cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }

    /*fun fetchAllPersons() {
        personModelListLiveData = personRepository?.fetchAllPersons()
    }*/

    /*fun fetchPersonsByQuery(query: String) {
        personModelListLiveData = personRepository?.fetchPersonsByQuery(query)
    }*/

    /*fun createPerson(personModel: PersonModel) {
        createPersonLiveData = personRepository?.createPerson(personModel)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

}
