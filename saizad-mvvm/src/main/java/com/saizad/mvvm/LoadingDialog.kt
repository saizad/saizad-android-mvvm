package com.saizad.mvvm

import android.app.Dialog
import android.content.Context
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.loading_dialog.*

class LoadingDialog(
    context: Context,
    @LayoutRes layoutRes: Int = R.layout.loading_dialog
) : Dialog(context) {

    var requestCounter = 0

    fun show(show: Boolean) {
        if (show) {
            requestCounter += 1
        } else {
            requestCounter -= 1
            requestCounter = requestCounter.coerceAtLeast(0)
        }

        if (show && requestCounter == 1) {
            show()
        } else if (!show && requestCounter == 0) {
            super.dismiss()
        } else {
            count.text = requestCounter.toString()
        }

        count.isVisible = requestCounter > 1
    }

    override fun dismiss() {
        requestCounter = 0
        super.dismiss()
    }

    init {
        setContentView(layoutRes)
        setCancelable(false)
    }
}