package com.saizad.mvvm.pager

import io.reactivex.Observable

interface BasePagerAdapterContract {
    fun pageLoaded(): Observable<Boolean>
}