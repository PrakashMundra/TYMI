package com.tymi.entity

import android.os.Parcel
import android.os.Parcelable

data class LookUp(val id: Int, val title: String) : Parcelable {
    override fun toString(): String {
        return title
    }

    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString())

    override fun writeToParcel(dest: Parcel?, p1: Int) {
        dest?.writeInt(id)
        dest?.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<LookUp> = object : Parcelable.Creator<LookUp> {
            override fun createFromParcel(source: Parcel): LookUp {
                return LookUp(source)
            }

            override fun newArray(size: Int): Array<LookUp?> {
                return arrayOfNulls(size)
            }
        }
    }
}