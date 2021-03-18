package com.saizad.mvvm

import android.app.Activity

class ActivityResult<V>(val requestCode: Int, val resultCode: Int, val value: V?) {

    constructor(requestCode: Int, value: V?) : this(requestCode, OK(), value) {}

    val isCanceled: Boolean
        get() = resultCode == Activity.RESULT_CANCELED

    val isOk: Boolean
        get() = resultCode == OK()

    fun isRequestCode(requestCode: Int): Boolean {
        return this.requestCode == requestCode
    }

    companion object {
        fun OK(): Int {
            return Activity.RESULT_OK
        }
    }

}