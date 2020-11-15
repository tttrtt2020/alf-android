package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.PersonModel
import com.example.alf.data.repository.PersonApiService

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private var personService: PersonApiService? = null
    var originalPersonLiveData: MutableLiveData<PersonModel>? = null
    var personLiveData: MutableLiveData<PersonModel>? = null
    /*var createPersonLiveData: LiveData<PersonModel>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    init {
        personService = PersonApiService()
        originalPersonLiveData = MutableLiveData()
        personLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/
    }

    fun fetchPersonById(id: Int) {
        personLiveData = personService?.fetchPersonById(id)
    }

    /*fun createPerson(personModel: PersonModel) {
        createPersonLiveData = personRepository?.createPerson(personModel)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

}
