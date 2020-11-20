package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.*
import com.example.alf.data.model.Person
import com.example.alf.data.repository.PersonApiService
import java.util.*

/**
 * ViewModel for the person create/edit screen.
 */
class PersonViewModel(application: Application, id: Int) : AndroidViewModel(application) {

    private var personService: PersonApiService = PersonApiService()

    var personLiveData: MutableLiveData<Person> = MutableLiveData()

    var getPersonResultLiveData: MutableLiveData<Boolean?> = Transformations.map(personLiveData) { p -> p != null } as MutableLiveData<Boolean?>
    var updatePersonResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    var deletePersonResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var personDataLiveData: LiveData<Boolean> = Transformations.map(personLiveData) { p -> p != null }

    init {
        loadingInProgressLiveData.addSource(personLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(updatePersonResultLiveData) { loadingInProgressLiveData.value = false }
        loadingInProgressLiveData.addSource(deletePersonResultLiveData) { loadingInProgressLiveData.value = false }

        getPersonById(id)
    }

    private fun getPersonById(id: Int) {
        loadingInProgressLiveData.value = true
        if (id == 0) {
            personLiveData.value = Person(0,
                    "", null, "",
                    null, null, null, null)
        } else {
            personService.getPersonById(personLiveData, id)
        }
    }

    fun updatePerson() {
        loadingInProgressLiveData.value = true
        personService.updatePerson(updatePersonResultLiveData, personLiveData.value!!)
    }

    fun deletePerson() {
        loadingInProgressLiveData.value = true
        personService.deletePerson(personLiveData, personLiveData.value!!)
    }

    fun getBirthDate(): Date? {
        return personLiveData.value?.birthDate
    }

    fun setBirthDate(time: Date) {
        personLiveData.value?.birthDate = time
    }

}
