package com.example.alf.ui.person

import androidx.lifecycle.*
import com.example.alf.AlfApplication
import com.example.alf.data.model.Country
import com.example.alf.data.model.Person
import com.example.alf.data.repository.PersonApiService
import java.util.*

/**
 * ViewModel for the person create/edit screen.
 */
class PersonViewModel(
        private val personId: Int,
        private val person: Person?
        ) : ViewModel() {

    private var personService: PersonApiService = PersonApiService()

    var personLiveData: MediatorLiveData<Person> = MediatorLiveData<Person>()
    //var personLiveData: MutableLiveData<Person> = MutableLiveData<Person>(person)

    var getPersonResultLiveData: MutableLiveData<Boolean?> = Transformations.map(personLiveData) { p -> p != null } as MutableLiveData<Boolean?>
    var createPersonLiveData: MutableLiveData<Person> = MutableLiveData<Person>()
    var createPersonResultLiveData: MutableLiveData<Boolean?> = Transformations.map(createPersonLiveData) { p -> p != null } as MutableLiveData<Boolean?>
    var updatePersonResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()
    var deletePersonResultLiveData: MutableLiveData<Boolean?> = MutableLiveData<Boolean?>()

    var loadingInProgressLiveData: MediatorLiveData<Boolean> = MediatorLiveData<Boolean>()
    var personDataLiveData: LiveData<Boolean> = Transformations.map(personLiveData) { p -> p != null }

    var photoUrlLiveData: LiveData<String> = Transformations.map(personLiveData) { p -> buildPhotoUrl(p) }

    private fun buildPhotoUrl(person: Person): String {
        return AlfApplication.getProperty("url.image.person") + person.id + AlfApplication.getProperty("extension.image.person")
    }

    init {
        loadingInProgressLiveData.apply {
            addSource(personLiveData) { loadingInProgressLiveData.value = false }
            addSource(createPersonLiveData) { loadingInProgressLiveData.value = false }
            addSource(updatePersonResultLiveData) { loadingInProgressLiveData.value = false }
            addSource(deletePersonResultLiveData) { loadingInProgressLiveData.value = false }
        }

        personLiveData.addSource(createPersonLiveData) {
            if (it != null) {
                personLiveData.value!!.id = it.id
            }
        }

        getPerson()
    }

    private fun getPerson() {
        loadingInProgressLiveData.value = true
        if (personId == 0) {
            // create new person
            personLiveData.value = Person(0,
                    "", null, "",
                    null, Country(0, "nj", "mrep"), null, null)
        } else {
            // get existing person
            personService.getPersonById(personLiveData, personId)
        }
    }

    fun createPerson() {
        loadingInProgressLiveData.value = true
        personService.createPerson(createPersonLiveData, personLiveData.value!!)
    }

    fun updatePerson() {
        loadingInProgressLiveData.value = true
        personService.updatePerson(updatePersonResultLiveData, personLiveData.value!!)
    }

    fun deletePerson() {
        loadingInProgressLiveData.value = true
        personService.deletePerson(deletePersonResultLiveData, personLiveData.value!!)
    }

    fun getBirthDate(): Date? {
        return personLiveData.value?.birthDate
    }

    fun setBirthDate(time: Date) {
        personLiveData.value?.birthDate = time
    }

    fun savePerson() {
        if (personLiveData.value!!.id == 0) {
            createPerson()
        } else {
            updatePerson()
        }
    }

}
