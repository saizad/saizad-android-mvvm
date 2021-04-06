package com.saizad.mvvmexample.components.main.profile.updateuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import com.shopify.livedataktx.SingleLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    mainEnvironment: MainEnvironment
) : MainViewModel(mainEnvironment){

    val form by lazy {
        ReqResUser.Form(savedStateHandle.get<ReqResUser>("user")!!)
    }

    fun save(): LiveData<ReqResUser>{
        val mutableLiveData = MutableLiveData<ReqResUser>()
        val freshUser = form.requiredBuild()
        currentUserType.refresh(freshUser)
        mutableLiveData.value = freshUser
        return mutableLiveData
    }
}