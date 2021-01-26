package com.example.alf.network.errorHandling

import java.util.*


data class ApiError(
        var status: String,
        var timestamp: String,
        var message: String,
        var debugMessage: String,
        var subErrors: List<ApiSubError>
)