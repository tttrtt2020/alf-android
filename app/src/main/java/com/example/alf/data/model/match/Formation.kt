package com.example.alf.data.model.match

data class Formation(
    var id: Int,
    var name: String,
    var fieldPositions: List<FieldPosition>
)