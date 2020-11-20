package com.example.alf.data.model

import java.io.Serializable

data class Country(
    var id: Int,
    var name: String,
    var officialName: String
): Serializable