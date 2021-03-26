package com.saizad.mvvmexample.components.main.users.userpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.components.main.MainPageViewModel
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserPageViewModel @Inject constructor(
    mainEnvironment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainPageViewModel(mainEnvironment, savedStateHandle){

    private lateinit var user: ReqResUser

    val liveData = MutableLiveData<ReqResUser>()

    fun setUser(resUser: ReqResUser){
        user = resUser
        liveData.postValue(user)
    }
}