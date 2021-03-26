package com.saizad.mvvm.pager

open class PageListenerImp<F: BasePagerAdapterContract>: PageListener<F> {
    override fun onPageReady(page: F) {
    }

    override fun onPageSelected(page: F) {
    }

    override fun pagePosition(position: Int) {
    }

    override fun upcomingPage(
        currentPosition: Int,
        newPosition: Int,
        newPositionVisiblePercent: Int
    ) {
    }

    override fun pageResetting(
        currentPosition: Int,
        tentativePosition: Int,
        tentativePositionPercent: Int
    ) {
    }

    override fun onPageLoaded(page: F, position: Int) {
    }
}