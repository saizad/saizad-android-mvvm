package com.saizad.mvvmexample.components.main.users

import android.os.Bundle
import android.view.View
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_users.*
import sa.zad.pagedrecyclerlist.ConstraintLayoutList
import sa.zad.pagedrecyclerlist.PageKeyedListDataSource

@AndroidEntryPoint
class UsersFragment : MainFragment<UsersViewModel>() {

    override val viewModelClassType: Class<UsersViewModel>
        get() = UsersViewModel::class.java

    override fun layoutRes(): Int {
        return R.layout.fragment_users
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list.init(viewLifecycleOwner, ConstraintLayoutList.CallbackPageKeyedList { next, callback ->
            viewModel().users(next, ConstraintLayoutList.CallBack {
                callback.call(
                    PageKeyedListDataSource.KeyDataCallback(
                        it.data,
                        it.previousPage(),
                        it.nextPage()
                    )
                )
            })
        })
    }
}
