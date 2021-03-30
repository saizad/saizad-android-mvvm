package com.saizad.mvvmexample.components.main.users.userpage

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.saizad.mvvm.utils.isFirstPage
import com.saizad.mvvm.utils.prev
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainPageFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_page.*
import kotlinx.android.synthetic.main.fragment_user_page_host.*

@AndroidEntryPoint
class UserPageFragment : MainPageFragment<UserPageViewModel>() {

    override val viewModelClassType: Class<UserPageViewModel>
        get() = UserPageViewModel::class.java

    override fun layoutRes(): Int {
        return R.layout.fragment_user_page
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel().liveData.observe(viewLifecycleOwner, Observer {
            fullName.text = it.fullName
            email.text = it.email
            Glide.with(requireContext())
                .load(it.avatar)
                .transform(CenterCrop())
                .into(bigAvatar)
        })
    }

    fun pageOnScreen(){
        lifecycleScope.launchWhenStarted {
            showShortToast("pageOnScreen $pageIndex")
        }
    }
}
