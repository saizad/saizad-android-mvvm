package com.saizad.mvvm.pager

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.saizad.mvvm.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers

open class BaseFragmentStateAdapter<F : BasePage<*>>(
    fm: FragmentActivity,
    private val items: List<Class<out F>>,
    viewPager: ViewPager2
) : FragmentStateAdapter(fm) {

    private var pageListener: PageListener<F>? = null
    private val fragments = SparseArray<F>()
    private var currentFragment: F? = null
    private var selected = -1

    init {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                selected = if (cb(position)) {
                    -1
                } else {
                    position
                }
            }
        })
    }

    private fun cb(position: Int): Boolean {
        fragments.get(position)?.let {
            it.onPageSelected()
            if (currentFragment != it) {
                currentFragment?.onPageUnSelected()
                if (it.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    pageListener?.onPageReady(it)
                } else {
                    return false
                }
            }
            currentFragment = it
            return true
        }
        return false
    }


    open fun setPageListener(pageListener: PageListener<F>) {
        this.pageListener = pageListener
    }

    override fun createFragment(position: Int): Fragment {
        val createInstance = Utils.createInstance(items[position])!!
        createInstance.pageIndex = position
        fragments.put(position, createInstance)
        createInstance.pageLoaded()
            .doOnNext {
                pageListener?.onPageLoaded(createInstance, position)
            }
            .filter { selected == position }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                cb(position)
            }
        return createInstance
    }

    override fun getItemCount(): Int = items.size

}