package com.example.alf.data.model

import java.io.Serializable

data class Team(
        var id: Int,
        var name: String,
        var club: Club
) : Serializable