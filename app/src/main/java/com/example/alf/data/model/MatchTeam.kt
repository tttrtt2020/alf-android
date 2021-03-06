package com.example.alf.data.model

import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.Appearance

data class MatchTeam(
        var team: Team,
        var formation: Formation?,
        var appearances: List<Appearance>
)