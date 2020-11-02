package com.example.alf.ui.persons

class PersonsPageModel {

    var content: List<PersonModel>? = null
    var last: Boolean = false
    var totalPages: Int = 0
    var totalElements: Int = 0
    var number: Int = 0
    var sort: Sort? = null
    var size: Int = 0
    var numberOfElements: Int = 0
    var first: Boolean = false
    var empty: Boolean = false

    class Sort {
        var unsorted: Boolean = false
        var sorted: Boolean = false
        var empty: Boolean = false
    }

}