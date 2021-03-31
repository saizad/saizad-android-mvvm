package com.saizad.mvvm.model

import android.os.Parcel
import android.os.Parcelable

open class IdModel(val id: Int) : Parcelable {

    constructor(parcel: Parcel) : this(parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<IdModel> {
        override fun createFromParcel(parcel: Parcel): IdModel {
            return IdModel(parcel)
        }

        override fun newArray(size: Int): Array<IdModel?> {
            return arrayOfNulls(size)
        }
    }
}