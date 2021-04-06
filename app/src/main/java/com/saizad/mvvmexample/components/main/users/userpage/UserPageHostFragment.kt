package com.saizad.mvvmexample.components.main.users.userpage

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.saizad.mvvm.pager.PageListenerImp
import com.saizad.mvvm.utils.isFirstPage
import com.saizad.mvvm.utils.next
import com.saizad.mvvm.utils.prev
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_user_page_host.*

@AndroidEntryPoint
class UserPageHostFragment : MainFragment<UserPageHostViewModel>() {

    val args: UserPageHostFragmentArgs by navArgs()

    override val viewModelClassType: Class<UserPageHostViewModel>
        get() = UserPageHostViewModel::class.java

    override fun layoutRes(): Int {
        return R.layout.fragment_user_page_host
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPageAdapter =
            UserPageAdapter(requireActivity(), args.users.map {
                UserPageFragment::class.java
            }, viewPager)
        viewPager.offscreenPageLimit = 3
        viewPager.adapter = userPageAdapter
        args.user?.let { user ->
            viewPager.setCurrentItem(args.users.indexOfFirst { it.id == user.id }, false)
        }
        viewPager.setPageTransformer(ZoomOutPageTransformer())

        userPageAdapter.setPageListener(object : PageListenerImp<UserPageFragment>() {
            override fun onPageLoaded(page: UserPageFragment, position: Int) {
                page.viewModel().setUser(args.users[position])
            }

            override fun onPageReady(page: UserPageFragment) {
                page.pageOnScreen()
            }
        })

        nextButton.throttleClick {
            viewPager.next(false)
        }
    }

    override fun onBackPressed(): Boolean {
        if(viewPager.isFirstPage) {
            return super.onBackPressed()
        }
        viewPager.prev()
        return true
    }
}
