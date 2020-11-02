package com.example.alf.ui.persons

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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

    init {
        personRepository = PersonRepository()
        personModelListLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/
    }

    fun fetchAllPersons() {
        personModelListLiveData = personRepository?.fetchAllPersons()
    }

    /*fun createPerson(personModel: PersonModel) {
        createPersonLiveData = personRepository?.createPerson(personModel)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

}
