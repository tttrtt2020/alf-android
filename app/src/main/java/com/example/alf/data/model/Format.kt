package com.example.alf.data.model

import java.io.Serializable

data class Format(
        var id: Int,
        var name: String,
        var playerCount: Int,
        var fieldSize: String,
        var targetSize: String,
        var fieldBackgroundUrl: String
) : Serializable