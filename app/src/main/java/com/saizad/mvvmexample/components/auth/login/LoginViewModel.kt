package com.saizad.mvvmexample.components.auth.login

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.saizad.mvvm.di.AssistedFactory
import com.saizad.mvvmexample.components.auth.AuthViewModel
import com.saizad.mvvmexample.di.auth.AuthEnvironment
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class LoginViewModel @AssistedInject constructor(
    authEnvironment: AuthEnvironment,
    @Assisted private val bundle: Bundle
) : AuthViewModel(authEnvironment){

    private val _toastLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: LiveData<String> get() = _toastLiveData

    private val args = LoginFragmentArgs.fromBundle(bundle)

    init {
        _toastLiveData.postValue(args.user.userName)
    }

    @dagger.assisted.AssistedFactory
    interface Fac : AssistedFactory<LoginViewModel> {
        override fun create(args: Bundle): LoginViewModel
    }
}