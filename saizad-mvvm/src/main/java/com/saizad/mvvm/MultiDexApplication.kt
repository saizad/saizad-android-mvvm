package com.saizad.mvvm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.multidex.MultiDex

open class MultiDexApplication : Application() {

    @CallSuper
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

}