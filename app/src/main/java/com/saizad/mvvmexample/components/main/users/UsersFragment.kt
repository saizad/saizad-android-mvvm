package com.saizad.mvvmexample.components.main.users

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.saizad.mvvm.utils.lifecycleScopeOnMain
import com.saizad.mvvm.utils.stateToData
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.components.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.coroutines.flow.collect
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
            lifecycleScopeOnMain {
                viewModel().users(next)
                    .stateToData()
                    .collect {
                        callback.call(
                            PageKeyedListDataSource.KeyDataCallback(
                                it.data,
                                it.previousPage(),
                                it.nextPage()
                            )
                        )
                    }
            }
        })

        list.setItemOnClickListener { item, itemView, itemIndex ->
            findNavController().navigate(
                UsersFragmentDirections.actionUsersFragmentToUserPageHostFragment(
                    list.listAdapter.items.toTypedArray(), item
                )
            )
        }
    }
}
