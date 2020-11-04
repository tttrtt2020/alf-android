package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.PersonModel
import com.example.alf.data.repository.PersonRepository

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private var personRepository: PersonRepository? = null
    var personModelLiveData: LiveData<PersonModel>? = null
    /*var createPersonLiveData: LiveData<PersonModel>? = null
    var deletePersonLiveData: LiveData<Boolean>? = null*/

    init {
        personRepository = PersonRepository()
        personModelLiveData = MutableLiveData()
        /*createPersonLiveData = MutableLiveData()
        deletePersonLiveData = MutableLiveData()*/
    }

    fun fetchPersonById(id: Int) {
        personModelLiveData = personRepository?.fetchPersonById(id)
    }

    /*fun createPerson(personModel: PersonModel) {
        createPersonLiveData = personRepository?.createPerson(personModel)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

}
