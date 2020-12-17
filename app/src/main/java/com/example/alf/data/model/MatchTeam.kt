package com.example.alf.data.model

import com.example.alf.data.model.match.Formation
import com.example.alf.data.model.match.MatchPerson

data class MatchTeam(
        var team: Team,
        var formation: Formation?,
        var matchPlayers: List<MatchPerson>
)