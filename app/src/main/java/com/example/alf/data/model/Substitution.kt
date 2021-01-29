package com.example.alf.data.model

data class Substitution(
        var id: Int?,
        var minute: Int,
        var team: Team,
        var playerOut: Player,
        var playerIn: Player,
) {
    enum class PlayerType {
        IN,
        OUT
    }
}
