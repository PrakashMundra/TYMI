package com.tymi.entity

import android.os.Parcel
import android.os.Parcelable

data class Incident(var id: String = "",
                    var statusId: String = "",
                    var profile: LookUp = LookUp("", ""),
                    var incident: LookUp = LookUp("", ""),
                    var cause: String = "",
                    var startDate: String = "",
                    var medication: String = "",
                    var notes: String = "",
                    var endDate: String = "",
                    var expenses: String = "",
                    var hospital: String = "") : Parcelable {

    constructor(source: Parcel) : this(
            source.readString(),
            source.readString(),
            source.readValue(LookUp::class.java.classLoader) as LookUp,
            source.readValue(LookUp::class.java.classLoader) as LookUp,
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeString(id)
        dest?.writeString(statusId)
        dest?.writeValue(profile)
        dest?.writeValue(incident)
        dest?.writeString(cause)
        dest?.writeString(startDate)
        dest?.writeString(medication)
        dest?.writeString(notes)
        dest?.writeString(endDate)
        dest?.writeString(expenses)
        dest?.writeString(hospital)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Incident> = object : Parcelable.Creator<Incident> {
            override fun createFromParcel(source: Parcel): Incident {
                return Incident(source)
            }

            override fun newArray(size: Int): Array<Incident?> {
                return arrayOfNulls(size)
            }
        }
    }
}