package com.saizad.mvvm.components

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.components.SaizadBaseViewModel.*
import com.saizad.mvvm.delegation.activity.ActivityAppLifeCycleCallback
import com.saizad.mvvm.delegation.activity.ActivityAppLifecycleDelegate
import com.saizad.mvvm.delegation.activity.ActivityAppLifecycleDelegateImp
import com.saizad.mvvm.delegation.activity.ActivityCB
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import rx.functions.Action1

abstract class SaizadBaseActivity<V : SaizadBaseViewModel> : AppCompatActivity(),
    ActivityAppLifecycleDelegate, ActivityCB<V>, ActivityAppLifeCycleCallback {


    protected val delegate: ActivityAppLifecycleDelegate

    init {
        delegate = ActivityAppLifecycleDelegateImp(this, this, javaClass.simpleName)
    }

    override fun context(): Context {
        return this
    }

    override fun viewModelStoreOwner(): ViewModelStoreOwner {
        return this
    }

    override val lifecycleOwner: LifecycleOwner
        get() = this

    abstract override val viewModelClassType: Class<V>

    override fun showLongToast(text: CharSequence) {
        delegate.showLongToast(text)
    }

    override fun showShortToast(value: Int) {
        delegate.showShortToast(value)
    }

    override fun showShortToast(text: CharSequence) {
        delegate.showShortToast(text)
    }

    override fun showToast(text: CharSequence, toastLength: Int) {
        delegate.showToast(text, toastLength)
    }

    override fun log(integer: Int) {
        delegate.log(integer)
    }

    override fun log(string: String) {
        delegate.log(string)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
        onViewReady()
    }

    override fun onViewReady() {
        delegate.onViewReady()
    }

    override fun requestError(errorData: ErrorData) {
        delegate.requestError(errorData)
    }

    override fun requestApiError(apiErrorData: ApiErrorData) {
        delegate.requestApiError(apiErrorData)
    }

    override fun requestLoading(loadingData: LoadingData) {
        delegate.requestLoading(loadingData)
    }

    override fun showLoading(show: Boolean) {
        delegate.showLoading(show)
    }

    override fun serverError(throwable: Throwable, requestId: Int): Boolean {
        return delegate.serverError(throwable, requestId)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        delegate.onStart()
    }

    @CallSuper
    override fun onStop() {
        super.onStop()
        delegate.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        delegate.onDestroy()
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        delegate.onResume()
    }

    override fun requestLocation(locationAction: Action1<Location>) {
        delegate.requestLocation(locationAction)
    }

    override fun showAlertDialogOk(
        title: String,
        message: String,
        cancelAble: Boolean
    ): LiveData<Int> {
        return delegate.showAlertDialogOk(title, message, cancelAble)
    }

    override fun showAlertDialogOk(
        title: String,
        message: String
    ): LiveData<Int> {
        return delegate.showAlertDialogOk(title, message, false)
    }

    override fun showAlertDialogYesNo(
        title: String,
        message: String,
        @DrawableRes icon: Int
    ): LiveData<Int> {
        return delegate.showAlertDialogYesNo(title, message, icon)
    }

    override fun showAlertDialogYesNo(
        title: String,
        message: String,
        @DrawableRes icon: Int,
        positiveName: String,
        negativeName: String
    ): LiveData<Int> {
        return delegate.showAlertDialogYesNo(title, message, icon, positiveName, negativeName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel().navigationFragmentResult(ActivityResult(requestCode, resultCode, data))
    }

    override fun viewModel(): V {
        return delegate.viewModel() as V
    }

    override fun compositeDisposable(): CompositeDisposable {
        return delegate.compositeDisposable()
    }

    override fun menRes(): Int {
        return 0
    }

    override fun openFragment(
        @IdRes fragment: Int,
        bundle: Bundle?,
        navOptions: NavOptions?
    ) {
        delegate.openFragment(fragment, bundle, navOptions)
    }

    @CallSuper
    override fun onBackPressed() {
        val navHostFragment =
            supportFragmentManager.primaryNavigationFragment as NavHostFragment?
        val primaryNavigationFragment =
            navHostFragment!!.childFragmentManager.primaryNavigationFragment
        if (primaryNavigationFragment is SaizadBaseFragment<*>) {
            if (!primaryNavigationFragment.onBackPressed()) {
                super.onBackPressed()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager()
            .onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    override fun bundle(): Bundle? {
        return intent.extras
    }

}