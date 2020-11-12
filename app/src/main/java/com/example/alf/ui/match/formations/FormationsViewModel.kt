package com.example.alf.ui.match.formations

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.alf.data.model.match.FormationModel
import com.example.alf.data.repository.FormationApiService

class FormationsViewModel(application: Application) : AndroidViewModel(application) {

    private var formationApiService: FormationApiService? = null
    var formationPersonsModelLiveData: LiveData<List<FormationModel>>? = null

    init {
        formationApiService = FormationApiService()
        formationPersonsModelLiveData = MutableLiveData()
    }

    fun fetchFormations() {
        formationPersonsModelLiveData = formationApiService?.fetchFormations()
    }

}
