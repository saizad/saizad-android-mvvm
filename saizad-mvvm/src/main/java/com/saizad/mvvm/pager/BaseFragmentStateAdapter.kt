package com.saizad.mvvm.pager

import android.util.SparseArray
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.saizad.mvvm.utils.Utils

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
                selected = position
                cb(position)
            }
        })
    }

    private fun cb(position: Int){
        fragments.get(position)?.let {
            it.onPageSelected()
            if (currentFragment != it) {
                currentFragment?.onPageUnSelected()
                pageListener?.onPageReady(it)
            }else if(currentFragment == null) {
                pageListener?.onPageReady(it)
            }
            currentFragment = it
        }
    }


    open fun setPageListener(pageListener: PageListener<F>) {
        this.pageListener = pageListener
    }

    override fun createFragment(position: Int): Fragment {
        val createInstance = Utils.createInstance(items[position])!!
        createInstance.pageIndex = position
        createInstance.pageLoaded()
            .subscribe {
                pageListener?.onPageLoaded(createInstance, position)
                if(selected == position){
                    cb(position)
                }
            }
        fragments.put(position, createInstance)
        return createInstance
    }

    override fun getItemCount(): Int = items.size

}