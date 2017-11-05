package com.tymi.entity

import android.os.Parcel
import android.os.Parcelable

data class Profile(var id: String = "",
                   var fullName: String = "",
                   var email: String = "",
                   var role: LookUp = LookUp("", ""),
                   var dateOfBirth: String = "") : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readString(),
            source.readValue(LookUp::class.java.classLoader) as LookUp,
            source.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeString(id)
        dest?.writeString(fullName)
        dest?.writeString(email)
        dest?.writeValue(role)
        dest?.writeString(dateOfBirth)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Profile> = object : Parcelable.Creator<Profile> {
            override fun createFromParcel(source: Parcel): Profile {
                return Profile(source)
            }

            override fun newArray(size: Int): Array<Profile?> {
                return arrayOfNulls(size)
            }
        }
    }
}