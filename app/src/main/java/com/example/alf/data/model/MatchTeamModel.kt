package com.example.alf.data.model

import com.example.alf.data.model.match.FormationModel

data class MatchTeamModel(
    var team: TeamModel,
    var formation: FormationModel
)