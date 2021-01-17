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

    private val personService: PersonApiService = PersonApiService()

    val personLiveData = MediatorLiveData<Person>()
    //var personLiveData: MutableLiveData<Person> = MutableLiveData<Person>(person)

    val getPersonResultLiveData = Transformations.map(personLiveData) { p -> p != null } as MutableLiveData<Boolean?>
    private val createPersonLiveData = MutableLiveData<Person>()
    val createPersonResultLiveData = Transformations.map(createPersonLiveData) { p -> p != null } as MutableLiveData<Boolean?>
    val updatePersonResultLiveData = MutableLiveData<Boolean?>()
    val deletePersonResultLiveData = MutableLiveData<Boolean?>()

    val loadingInProgressLiveData = MediatorLiveData<Boolean>()
    val personDataLiveData = Transformations.map(personLiveData) { p -> p != null }

    val photoUrlLiveData = Transformations.map(personLiveData) { p -> buildPhotoUrl(p) }

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
            personService.getPersonById(
                    personId,
                    { personLiveData.value = it },
                    { personLiveData.value = null }
            )
        }
    }

    fun createPerson() {
        loadingInProgressLiveData.value = true
        personService.createPerson(
                personLiveData.value!!,
                { createPersonLiveData.value = it },
                { createPersonLiveData.value = null }
        )
    }

    fun updatePerson() {
        loadingInProgressLiveData.value = true
        personService.updatePerson(
                personLiveData.value!!,
                { updatePersonResultLiveData.value = true },
                { updatePersonResultLiveData.value = false }
        )
    }

    fun deletePerson() {
        loadingInProgressLiveData.value = true
        personService.deletePerson(
                personLiveData.value!!,
                { deletePersonResultLiveData.value = true },
                { deletePersonResultLiveData.value = false }
        )
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
