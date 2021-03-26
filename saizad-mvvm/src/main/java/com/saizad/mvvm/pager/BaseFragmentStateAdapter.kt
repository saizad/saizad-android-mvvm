package com.saizad.mvvm.pager

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.saizad.mvvm.utils.Utils

open class BaseFragmentStateAdapter<F : BasePage<*>>(
    fm: FragmentActivity,
    private val items: List<Class<out F>>
) : FragmentStateAdapter(fm) {

    private var pageListener: PageListener<F>? = null

    open fun setPageListener(pageListener: PageListener<F>) {
        this.pageListener = pageListener
    }

    override fun createFragment(position: Int): Fragment {
        Log.d("safdasdfasdfa", "$position")
        val createInstance = Utils.createInstance(items[position])!!
        createInstance.pageLoaded()
            .subscribe {
                pageListener?.onPageLoaded(createInstance, position)
            }
        return createInstance
    }

    override fun getItemCount(): Int = items.size


}