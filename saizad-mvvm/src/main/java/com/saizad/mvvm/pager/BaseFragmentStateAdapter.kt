package com.saizad.mvvm.pager

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.saizad.mvvm.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

open class BaseFragmentStateAdapter<F : BasePage<*>>(
    private val hostFragment: Fragment,
    private val items: List<Class<out F>>,
    viewPager: ViewPager2
) : FragmentStateAdapter(hostFragment) {

    private var pageListener: PageListener<F>? = null
    var currentPage: F? = null
    private var selected = -1

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selected = if (makeCall(position)) {
                    -1
                } else {
                    position
                }
            }
        })
    }

    private fun makeCall(position: Int): Boolean {
        val fragments = hostFragment.childFragmentManager.fragments
        fragments.forEach {
            if (it is BasePage<*>) {
                if (it.pageIndex == position && currentPage != it) {
                    currentPage?.onPageUnSelected()
                    if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        pageListener?.onPageReady(it as F)
                    } else {
                        return false
                    }
                    currentPage = it as F
                    return true
                }
            }
        }
        return false
    }

    open fun setPageListener(pageListener: PageListener<F>) {
        this.pageListener = pageListener
    }

    override fun createFragment(position: Int): Fragment {
        val createInstance = Utils.createInstance(items[position])!!
        createInstance.pageIndex = position
        createInstance.pageLoaded()
            .doOnNext {
                pageListener?.onPageLoaded(createInstance, position)
            }
            .filter { selected == position }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                makeCall(position)
            }
        return createInstance
    }

    override fun getItemCount(): Int = items.size

}