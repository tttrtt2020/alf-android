package com.example.alf.ui.match.formations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.Formation
import com.example.alf.data.repository.FormationApiService

class FormationsViewModel(application: Application) : AndroidViewModel(application) {

    private var formationApiService: FormationApiService? = null
    var formationPersonsLiveData: LiveData<List<Formation>>? = null

    init {
        formationApiService = FormationApiService()
        formationPersonsLiveData = MutableLiveData()
    }

    fun fetchFormations() {
        formationPersonsLiveData = formationApiService?.fetchFormations()
    }

}
