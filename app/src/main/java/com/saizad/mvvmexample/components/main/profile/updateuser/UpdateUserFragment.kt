package com.saizad.mvvmexample.components.main.profile.updateuser

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.utils.addToComposite
import com.saizad.mvvm.utils.lifecycleScopeOnMain
import com.saizad.mvvm.utils.throttleClick
import com.saizad.mvvmexample.MVVMExampleCurrentUser
import com.saizad.mvvmexample.R
import com.saizad.mvvmexample.RequestCodes
import com.saizad.mvvmexample.components.main.MainFragment
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_update_user.*
import javax.inject.Inject

@AndroidEntryPoint
class UpdateUserFragment : MainFragment<UpdateUserViewModel>() {

    override val viewModelClassType: Class<UpdateUserViewModel>
        get() = UpdateUserViewModel::class.java

    override fun layoutRes(): Int {
        return R.layout.fragment_update_user
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val form = viewModel().form
        fullNameField.setField(form.fullNameField)
        jobField.setField(form.jobField)

        save.throttleClick {
            viewModel().save().observe(viewLifecycleOwner, Observer {
                finishWithResult(ActivityResult(RequestCodes.USER, it))
            })
        }

        form.validObservable()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                save.isEnabled = it
            }.addToComposite(this)

    }
}
