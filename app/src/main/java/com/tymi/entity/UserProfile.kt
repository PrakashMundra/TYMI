package com.tymi.entity

data class UserProfile(var fullName: String = "",
                       var role: LookUp = LookUp("", ""),
                       var dateOfBirth: String = "")