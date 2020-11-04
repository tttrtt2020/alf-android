package com.example.alf.data.model

import java.util.*

data class PersonModel(
    var id: Int? = 0,
    var firstName: String? = "",
    var patronymic: String? = "",
    var lastName: String? = "",
    val birthDate: Date?,
    var country: CountryModel?,
    var height: Int? = 0,
    var weight: Int? = 0
) {
    data class CountryModel(
        var id: Int? = 0,
        var name: String? = "",
        var officialName: String? = ""
    )
}

