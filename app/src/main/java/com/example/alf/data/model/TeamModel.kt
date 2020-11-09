package com.example.alf.data.model

data class TeamModel (
    var id: Int,
    var name: String,
    var clubId: Int,
    var club: ClubModel
)