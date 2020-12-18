package com.example.alf.data.model.match

import com.example.alf.data.model.Player

data class MatchPlayer (
        var player: Player,
        var playerRole: Role,
        var fieldPosition: FieldPosition?,
        var timeIn: Int?,
        var timeOut: Int?,
        var inForPlayer: Player?,
        var outForPlayer: Player?
)