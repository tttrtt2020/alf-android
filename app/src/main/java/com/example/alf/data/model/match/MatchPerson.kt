package com.example.alf.data.model.match

data class MatchPerson (
    var player: Player,
    var playerRole: Role,
    var fieldPosition: FieldPosition?,
    var timeIn: Int,
    var timeOut: Int,
    var inForPlayer: Player?,
    var outForPlayer: Player?
)