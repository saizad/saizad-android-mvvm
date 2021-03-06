package com.saizad.mvvm.di

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

interface AssistedFactory<VM : ViewModel> {

    companion object {
        fun <VM : ViewModel, AS : AssistedFactory<VM>> provideFactory(
            assistedFactory: AS,
            args: Bundle
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return assistedFactory.create(args) as T
            }
        }
    }

    fun create(args: Bundle): VM
}