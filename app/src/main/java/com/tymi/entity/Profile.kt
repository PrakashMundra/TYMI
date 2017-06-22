package com.tymi.entity

data class Profile(var id: String = "",
                   var fullName: String = "",
                   var email: String = "",
                   var role: LookUp = LookUp("", ""),
                   var dateOfBirth: String = "")