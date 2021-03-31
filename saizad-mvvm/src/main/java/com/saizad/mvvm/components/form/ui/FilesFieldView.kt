package com.saizad.mvvm.components.form.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.sa.easyandroidform.field_view.BaseFieldView
import com.sa.easyandroidform.fields.StringListField
import com.saizad.mvvm.SaizadRequestCodes
import com.saizad.mvvm.components.SaizadBaseViewModel
import com.saizad.mvvm.utils.throttleClick
import sa.zad.easypermission.AppPermission
import sa.zad.easypermission.AppPermissionRequest
import sa.zad.easypermission.PermissionManager
import sa.zad.easyretrofit.base.ProgressObservable
import sa.zad.easyretrofit.observables.UploadObservable
import java.io.File


abstract class FilesFieldView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseFieldView<List<String>>(context, attrs, defStyleAttr) {

    private lateinit var parentFragment: Fragment
    private lateinit var baseViewModel: SaizadBaseViewModel
    protected lateinit var filesField: StringListField
    private lateinit var permissionManager: PermissionManager
    private lateinit var storagePermission: AppPermission
    private var undo: View? = null
    private var clear: View? = null

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        undo = undoView()
        clear = clearView()

        initView().throttleClick {
            init()
        }
        if (undo != null) {
            undo!!.throttleClick {
                reset()
            }
        }
        if (clear != null) {
            clear!!.throttleClick {
                filesField.field = null
            }
        }
    }

    fun reset() {
        filesField.field = filesField.ogField
        showValue(filesField.ogField)
    }

    fun setup(
        fragment: Fragment,
        baseViewModel: SaizadBaseViewModel,
        permissionManager: PermissionManager,
        permCode: Int,
        fileField: StringListField
    ) {
        this.storagePermission = permissionManager.getAppPermission(permCode)
        parentFragment = fragment
        this.baseViewModel = baseViewModel
        this.permissionManager = permissionManager
        this.filesField = fileField
        setField(fileField)
        init(baseViewModel)

    }

    fun <T> upload(uploadObservable: UploadObservable<T>): ProgressObservable<T> {
        return uploadObservable
            .onProgressStart({
            }, 1000, 1000, 100)
            .progressUpdate {
            }
            .onProgressCompleted {
            }
    }

    private fun init(baseViewModel: SaizadBaseViewModel) {
        baseViewModel.onNavigationResult(SaizadRequestCodes.PICK_FILE, Intent::class.java)
            .observe(parentFragment.viewLifecycleOwner, Observer {
                val list = ArrayList<String>()
                // checking multiple selection or not
                if (null != it.getClipData()) {
                    for (i in 0 until it.clipData!!.itemCount) {
                        val uri: Uri = it.clipData!!.getItemAt(i).uri
                        list.add(extractFile(uri).absolutePath)
                    }
                } else {
                    val uri: Uri = it.data!!
                    list.add(extractFile(uri).absolutePath)
                }
                filesField.field = list

            })
    }

    private fun extractFile(uri: Uri): File {
        val fileName = getFileName(uri)
        val extension = File(fileName).extension
        val file =
            File.createTempFile((111111..9999999).random().toString(), ".$extension")
        sa.zad.easyretrofit.Utils.writeStreamToFile(
            context.contentResolver.openInputStream(uri)!!,
            file
        )
        return file
    }

    protected fun init() {
        requestPerm()
    }

    private fun requestPerm() {
        permissionManager.requestPermission(parentFragment, storagePermission.permissionCode)
            .filter {
                if (it.requestStatus == AppPermissionRequest.REQUEST_CANCELED) {
                    //open app setting page
                }
                it.requestStatus == AppPermissionRequest.ALL_READY_GRANTED || it.requestStatus == AppPermissionRequest.GRANTED
            }
            ?.subscribe({
                openPicker()
            }, {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            })
    }

    private fun getRealPathFromURIPath(contentURI: Uri): String {
        val cursor = this.context?.contentResolver?.query(contentURI, null, null, null, null)
        return if (cursor == null) {
            contentURI.path!!
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            val path = cursor.getString(idx)
            cursor.close()
            path
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = this.context.contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor!!.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result!!.lastIndexOf('/')
            if (cut != -1) {
                result = result.substring(cut + 1)
            }
        }
        return result
    }

    private fun openPicker() {
        parentFragment.activity?.startActivityForResult(intent(), SaizadRequestCodes.PICK_FILE)
    }

    abstract fun intent(): Intent

    override fun displayError(show: Boolean, error: String?) {

    }

    override fun notSetError(error: String) {
    }

    @CallSuper
    override fun showValue(field: List<String>?) {
        if (undo != null) {
            undo!!.isVisible = filesField.isModified
        }
        if (clear != null) {
            clear!!.isVisible =
                !filesField.isMandatory && field != null && filesField.ogField != null
        }
    }

    override fun fieldMandatory() {
        if (clear != null) {
            clear!!.isVisible = false
        }
    }

    open fun clearView(): View? {
        return null
    }

    open fun undoView(): View? {
        return null
    }

    abstract fun initView(): View
}
