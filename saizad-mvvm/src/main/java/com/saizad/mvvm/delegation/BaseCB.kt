package com.saizad.mvvm.delegation

import android.content.Context
import android.os.Bundle
import androidx.annotation.MenuRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.saizad.mvvm.Environment
import com.saizad.mvvm.SaizadLocation
import com.saizad.mvvm.components.SaizadBaseViewModel

interface BaseCB<V : SaizadBaseViewModel> {

    val lifecycleOwner: LifecycleOwner
    fun context(): Context
    fun viewModelStoreOwner(): ViewModelStoreOwner
    val viewModelClassType: Class<V>
    fun appLocation(): SaizadLocation
    fun environment(): Environment

    @MenuRes
    fun menRes(): Int
    fun navController(): NavController
    fun bundle(): Bundle?
}