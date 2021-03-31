package com.saizad.mvvm.pager

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import com.saizad.mvvm.components.SaizadBaseFragment
import com.saizad.mvvm.components.SaizadBaseViewModel
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject


abstract class BasePage<VM : SaizadBaseViewModel> : SaizadBaseFragment<VM>(),
    PagerAdapterListener, BasePagerAdapterContract {

    private var pageLoaded = BehaviorSubject.create<Boolean>()
    var pageIndex : Int = -1

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {
        super.onViewCreated(view, savedInstanceState, recycled)
        pageLoaded.onNext(true)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        pageLoaded.onComplete()
        pageLoaded = BehaviorSubject.create()
    }

    override fun onPageSelected() {
        log("onPageSelected $this")
    }

    override fun onPageUnSelected() {
        log("onPageUnSelected $this")
    }

    override fun pageLoaded(): Observable<Boolean> {
        return pageLoaded
    }
}
