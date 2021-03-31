package com.saizad.mvvm.pager

open class PageListenerImp<F: BasePagerAdapterContract>: PageListener<F> {
    override fun onPageReady(page: F) {
    }

    override fun onPageLoaded(page: F, position: Int) {
    }
}