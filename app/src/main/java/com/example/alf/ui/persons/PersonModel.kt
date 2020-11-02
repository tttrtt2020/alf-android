package com.example.alf.ui.persons

import java.time.LocalDate
import java.util.*

data class PersonModel(
    var id: Int? = 0,
    var firstName: String? = "",
    var patronymic: String? = "",
    var lastName: String? = "",
    var birthDate: Date?,
    //var country: CountryModel? = ""
    var height: Int? = 0,
    var weight: Int? = 0
)