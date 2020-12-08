package com.example.alf.data.model.match

data class MatchPerson (
    var player: Player,
    var playerRole: Role,
    var fieldPosition: FieldPosition?,
    var inForPlayer: Player?,
    var outForPlayer: Player?
)