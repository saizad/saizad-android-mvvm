package com.saizad.mvvmexample.components.main.profile.updateuser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UpdateUserViewModel @Inject constructor(
    mainEnvironment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainEnvironment, savedStateHandle){

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