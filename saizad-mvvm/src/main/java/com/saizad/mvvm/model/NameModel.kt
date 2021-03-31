package com.saizad.mvvm.model

import android.os.Parcel
import android.os.Parcelable

open class NameModel(id: Int = 0, var name: String) : IdModel(id), Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString()!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NameModel> {
        override fun createFromParcel(parcel: Parcel): NameModel {
            return NameModel(parcel)
        }

        override fun newArray(size: Int): Array<NameModel?> {
            return arrayOfNulls(size)
        }
    }
}