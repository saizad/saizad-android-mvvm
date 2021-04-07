package com.saizad.mvvmexample.components.main.users.userpage

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvmexample.components.main.MainViewModel
import com.saizad.mvvmexample.di.main.MainEnvironment
import com.saizad.mvvmexample.models.ReqResUser
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserPageHostViewModel @Inject constructor(
    mainEnvironment: MainEnvironment,
    savedStateHandle: SavedStateHandle
) : MainViewModel(mainEnvironment){

    private val _initLiveData = MutableLiveData<Pair<List<ReqResUser>, ReqResUser?>>()
    val initLiveData: MutableLiveData<Pair<List<ReqResUser>, ReqResUser?>>
        get() = _initLiveData

    private val users = savedStateHandle.get<Array<ReqResUser>>("users")!!.toList()
    private var currentSelected = savedStateHandle.get<ReqResUser>("user")


    override fun onViewCreated() {
        super.onViewCreated()
        initLiveData.value = users.subList(0,10) to currentSelected
    }

    fun setCurrentUser(currentUser: ReqResUser){
        currentSelected = currentUser
    }

}