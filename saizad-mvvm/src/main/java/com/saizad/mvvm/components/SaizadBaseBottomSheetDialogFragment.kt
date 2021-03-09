package com.saizad.mvvm.components

import android.content.Context
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.annotation.CallSuper
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saizad.mvvm.ActivityResult
import com.saizad.mvvm.components.SaizadBaseViewModel.*
import com.saizad.mvvm.delegation.fragment.FragmentAppLifecycleCallBack
import com.saizad.mvvm.delegation.fragment.FragmentAppLifecycleDelegate
import com.saizad.mvvm.delegation.fragment.FragmentAppLifecycleDelegateImp
import com.saizad.mvvm.delegation.fragment.FragmentCB
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import rx.functions.Action1

abstract class SaizadBaseBottomSheetDialogFragment<V : SaizadBaseViewModel> :
    BottomSheetDialogFragment(), FragmentAppLifecycleDelegate, FragmentCB<V>,
    FragmentAppLifecycleCallBack {

    protected var delegate: FragmentAppLifecycleDelegate =
        FragmentAppLifecycleDelegateImp(this, this, javaClass.simpleName)

    override fun context(): Context {
        return requireContext()
    }

    override fun viewModelStoreOwner(): ViewModelStoreOwner {
        return this
    }

    abstract override val viewModelClassType: Class<V>

    override fun navController(): NavController {
        return Navigation.findNavController(requireView())
    }

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

    override fun <T> finishWithResult(activityResult: ActivityResult<T>) {
        delegate.finishWithResult(activityResult)
    }

    override fun finish() {
        delegate.finish()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        delegate.onViewCreated(view, savedInstanceState)
    }

    override fun onViewReady() {}

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        delegate.onCreateOptionsMenu(menu, inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?, recycled: Boolean) {}

    override fun setHasOptionsMenu(hasMenu: Boolean) {
        super.setHasOptionsMenu(hasMenu)
    }

    override fun onBackPressed(): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        delegate.onCreate(savedInstanceState)
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
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        delegate.onActivityCreated(savedInstanceState)
    }

    @CallSuper
    override fun onPause() {
        super.onPause()
        delegate.onPause()
    }

    @CallSuper
    override fun onAttach(context: Context) {
        super.onAttach(context)
        delegate.onAttach(context)
    }

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return delegate.onCreateView(inflater, container, savedInstanceState)
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
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        delegate.onViewStateRestored(savedInstanceState)
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

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        delegate.onDestroyView()
    }

    @CallSuper
    override fun onDetach() {
        super.onDetach()
        delegate.onDetach()
    }

    override fun showAlertDialogOk(
        title: String,
        message: String,
        cancelAble: Boolean
    ): LiveData<Int> {
        return delegate.showAlertDialogOk(title, message, cancelAble)
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

    fun openFragment(@IdRes fragment: Int) {
        delegate.openFragment(fragment)
    }

    fun openFragment(@IdRes fragment: Int, bundle: Bundle?) {
        delegate.openFragment(fragment, bundle)
    }

    override fun openFragment(
        @IdRes fragment: Int,
        bundle: Bundle?,
        navOptions: NavOptions?
    ) {
        delegate.openFragment(fragment, bundle, navOptions)
    }

    override fun requestLocation(locationAction: Action1<Location>) {
        delegate.requestLocation(locationAction)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionManager()
            .onRequestPermissionsResult(activity, requestCode, permissions, grantResults)
    }

    override fun viewModel(): V {
        return delegate.viewModel() as V
    }

    override fun compositeDisposable(): CompositeDisposable {
        return delegate.compositeDisposable()
    }

    override fun persistView(): Boolean {
        return true
    }

    override fun menRes(): Int {
        return 0
    }

}