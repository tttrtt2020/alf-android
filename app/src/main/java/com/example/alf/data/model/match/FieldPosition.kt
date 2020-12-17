package com.example.alf.data.model.match

import java.io.Serializable

data class FieldPosition(
        var id: Int,
        var name: String,
        var shortName: String,
        var lengthValue: Double,
        var widthValue: Double,
) : Serializable
