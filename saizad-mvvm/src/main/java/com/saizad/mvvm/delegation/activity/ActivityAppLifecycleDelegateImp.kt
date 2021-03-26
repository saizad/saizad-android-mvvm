package com.saizad.mvvm.delegation.activity

import com.saizad.mvvm.LoadingDialog
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.delegation.BaseLifecycleDelegateImp
import com.saizad.mvvm.utils.addToDisposable
import io.reactivex.android.schedulers.AndroidSchedulers

class ActivityAppLifecycleDelegateImp<V : SaizadBaseViewModel>(
    val activityAppLifeCycleCallback: ActivityAppLifeCycleCallback,
    activityCB: ActivityCB<V>,
    tag: String
) : BaseLifecycleDelegateImp<V, ActivityCB<V>>(activityAppLifeCycleCallback, activityCB, tag),
    ActivityAppLifecycleDelegate{

    val loadingDialog: LoadingDialog by lazy {
        LoadingDialog(appLifecycleDelegate.context())
    }

    override fun requestLoading(loadingData: SaizadBaseViewModel.LoadingData) {
        super.requestLoading(loadingData)
        loadingDialog.show(loadingData.isLoading)
    }

    override fun showLoading(show: Boolean) {
        super.showLoading(show)
        loadingDialog.show(show)
    }

    override fun onDestroy() {
        super.onDestroy()
        loadingDialog.dismiss()
    }

    override fun onViewReady() {
        super.onViewReady()
        viewModel.loadingSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                activityAppLifeCycleCallback.requestLoading(it)
            }.addToDisposable(compositeDisposable)
    }
}