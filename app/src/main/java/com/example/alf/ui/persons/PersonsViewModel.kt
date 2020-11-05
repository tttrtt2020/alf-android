package com.example.alf.ui.persons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.example.alf.data.model.PersonModel
import com.example.alf.data.paging.PersonsBackendService
import com.example.alf.data.paging.PersonsPagingSource
import com.example.alf.data.repository.PersonRepository

/*
class PersonsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is persons Fragment"
    }
    val text: LiveData<String> = _text
}*/

class PersonsViewModel(application: Application) : AndroidViewModel(application) {

    private var personRepository: PersonRepository? = null
    var personModelListLiveData: LiveData<List<PersonModel>>? = null
    /*var createPersonLiveData: LiveData<PersonModel>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    lateinit var query: String

    val flow = Pager(PagingConfig(pageSize = 20)) {
        PersonsPagingSource(PersonsBackendService(), query)
    }.flow.cachedIn(viewModelScope)

    init {
        personRepository = PersonRepository()
        personModelListLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/

        query = ""
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
