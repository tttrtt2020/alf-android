package com.example.alf.ui.person

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.CountryModel
import com.example.alf.data.model.PersonModel
import com.example.alf.data.model.deepCopy
import com.example.alf.data.repository.PersonApiService
import java.util.*

class PersonViewModel(application: Application) : AndroidViewModel(application) {

    private var personService: PersonApiService = PersonApiService()
    private var originalPerson: PersonModel? = null
    private var personLiveData: MutableLiveData<PersonModel> = MutableLiveData()
    /*var createPersonLiveData: LiveData<PersonModel>? = MutableLiveData()
    var deletePersonLiveData: LiveData<Boolean>? = MutableLiveData()*/

    var saveEnabledLiveData: MutableLiveData<Boolean> = MutableLiveData()

    fun getPersonLiveData(id: Int): MutableLiveData<PersonModel> {
        fetchPersonById(id)
        return personLiveData
    }

    private fun fetchPersonById(id: Int) {
        personLiveData = personService.fetchPersonById(id)!!
    }

    /*fun createPerson(personModel: PersonModel) {
        createPersonLiveData = personRepository?.createPerson(personModel)
    }

    fun deletePerson(id: Int) {
        deletePersonLiveData = personRepository?.deletePerson(id)
    }*/

    fun onPersonDataChanged() {
        saveEnabledLiveData.value = isPersonChanged()
    }

    fun isPersonChanged(): Boolean {
        return originalPerson != personLiveData.value
    }

    fun saveOriginal(personModel: PersonModel) {
        if (originalPerson == null) {
            originalPerson = deepCopy(personModel)
        }
    }

    fun getBirthDate(): Date? {
        return personLiveData.value?.birthDate
    }

    fun setBirthDate(time: Date) {
        personLiveData.value?.birthDate = time
        onPersonDataChanged()
    }

    fun getFirstName(): String? {
        return personLiveData.value?.firstName
    }

    fun setFirstName(firstName: String) {
        personLiveData.value?.firstName = firstName
        onPersonDataChanged()
    }

    fun setPatronymic(patronymic: String) {
        personLiveData.value?.patronymic = patronymic
        onPersonDataChanged()
    }

    fun setLastName(lastName: String) {
        personLiveData.value?.lastName = lastName
        onPersonDataChanged()
    }

    fun setCountry(country: CountryModel) {
        personLiveData.value?.country = country
        onPersonDataChanged()
    }

    fun setHeight(height: Int) {
        personLiveData.value?.height = height
        onPersonDataChanged()
    }

    fun setWeight(weight: Int) {
        personLiveData.value?.weight = weight
        onPersonDataChanged()
    }

}
