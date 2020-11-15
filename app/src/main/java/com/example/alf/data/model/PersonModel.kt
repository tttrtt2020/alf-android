package com.example.alf.data.model

import java.util.*

data class PersonModel(
    var id: Int,
    var firstName: String,
    var patronymic: String?,
    var lastName: String?,
    var birthDate: Date?,
    var country: CountryModel?,
    var height: Int?,
    var weight: Int?,
)