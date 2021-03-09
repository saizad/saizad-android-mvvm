package com.saizad.mvvmexample.components.auth.blank

import com.saizad.mvvmexample.components.auth.AuthFragment
import com.saizad.mvvmexample.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BlankFragment : AuthFragment<BlankViewModel>() {

    override val viewModelClassType: Class<BlankViewModel>
        get() = BlankViewModel::class.java


    override fun layoutRes(): Int {
        return R.layout.fragment_blank
    }
}
