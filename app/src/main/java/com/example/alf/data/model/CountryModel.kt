package com.example.alf.data.model

import java.io.Serializable

data class CountryModel(
    var id: Int,
    var name: String,
    var officialName: String
): Serializable