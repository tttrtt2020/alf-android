package com.example.alf.data.model

data class PersonsPageModel (
    var content: List<PersonModel>,
    var last: Boolean,
    var totalPages: Int,
    var totalElements: Int,
    var number: Int,
    var sort: Sort?,
    var size: Int,
    var numberOfElements: Int,
    var first: Boolean,
    var empty: Boolean
) {
    class Sort {
        var unsorted: Boolean = false
        var sorted: Boolean = false
        var empty: Boolean = false
    }
}