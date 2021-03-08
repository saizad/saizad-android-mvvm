package com.saizad.mvvmexample.di.main

import com.saizad.mvvm.model.UserInfo
import dagger.BindsInstance
import dagger.hilt.DefineComponent
import dagger.hilt.components.SingletonComponent

@LoggedUserScope
@DefineComponent(parent = SingletonComponent::class)
interface UserComponent {

    @DefineComponent.Builder
    interface Builder {
        fun setUser(@BindsInstance user: UserInfo): Builder
        fun build(): UserComponent
    }
}