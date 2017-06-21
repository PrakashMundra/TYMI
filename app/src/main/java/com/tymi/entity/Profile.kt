package com.tymi.entity

data class Profile(var id: Int,
                   var fullName: String,
                   var email: String,
                   var role: LookUp,
                   var dateOfBirth: String)