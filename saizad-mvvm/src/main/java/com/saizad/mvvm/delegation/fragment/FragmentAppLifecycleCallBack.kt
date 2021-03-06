package com.saizad.mvvm.delegation.fragment

import android.os.Bundle
import android.view.View
import com.saizad.mvvm.delegation.BaseLifecycleCallBack

interface FragmentAppLifecycleCallBack : BaseLifecycleCallBack {
    fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean)
    fun setHasOptionsMenu(hasOptionsMenu: Boolean)
    fun onBackPressed(): Boolean
}