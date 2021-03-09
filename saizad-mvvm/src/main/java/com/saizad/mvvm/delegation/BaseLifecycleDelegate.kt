package com.saizad.mvvm.delegation

import android.location.Location
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.components.SaizadBaseViewModel.*
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import rx.functions.Action1

interface BaseLifecycleDelegate {
    fun onCreate(savedInstanceState: Bundle?)
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
    fun showLongToast(text: CharSequence)
    fun showShortToast(value: Int)
    fun showShortToast(text: CharSequence)
    fun showToast(text: CharSequence, toastLength: Int)
    fun log(integer: Int)
    fun log(string: String)
    fun requestError(errorData: ErrorData)
    fun requestApiError(apiErrorData: ApiErrorData)
    fun requestLoading(loadingData: LoadingData)
    fun showLoading(show: Boolean)
    fun serverError(throwable: Throwable, requestId: Int): Boolean
    fun showAlertDialogOk(
        title: String,
        message: String,
        cancelAble: Boolean = false
    ): LiveData<Int>

    fun showAlertDialogYesNo(
        title: String,
        message: String,
        @DrawableRes icon: Int,
        positiveName: String = "Yes",
        negativeName: String = "No"
    ): LiveData<Int>

    fun requestLocation(locationAction: Action1<Location>)
    fun viewModel(): SaizadBaseViewModel
    fun compositeDisposable(): CompositeDisposable
    fun navController(): NavController
    fun openFragment(
        @IdRes fragment: Int,
        bundle: Bundle? = null,
        navOptions: NavOptions? = null
    )

    fun onViewReady()
}