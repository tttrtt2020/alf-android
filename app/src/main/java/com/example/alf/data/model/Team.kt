package com.example.alf.data.model

data class Team (
    var id: Int,
    var name: String,
    var clubId: Int,
    var club: Club
)