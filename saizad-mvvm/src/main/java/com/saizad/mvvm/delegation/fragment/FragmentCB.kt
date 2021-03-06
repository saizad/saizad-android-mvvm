package com.saizad.mvvm.delegation.fragment

import androidx.annotation.LayoutRes
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.delegation.BaseCB

interface FragmentCB<V : SaizadBaseViewModel> : BaseCB<V> {
    @LayoutRes
    fun layoutRes(): Int
    fun persistView(): Boolean
}