package com.castcle.android.core.base.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.WindowManager
import androidx.viewbinding.ViewBinding
import com.castcle.android.BuildConfig
import com.castcle.android.R
import com.castcle.android.core.extensions.string
import com.castcle.android.core.extensions.toast
import io.reactivex.disposables.CompositeDisposable

@Suppress("LeakingThis")
open class BaseDialog(
    binding: ViewBinding,
    isCancelable: Boolean = true,
    height: Double? = null,
    width: Double? = null,
) : Dialog(binding.root.context) {

    init {
        setCancelable(isCancelable)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setLayout(
            width
                ?.run { context.resources.displayMetrics.widthPixels.times(width).toInt() }
                ?: WindowManager.LayoutParams.WRAP_CONTENT,
            height
                ?.run { context.resources.displayMetrics.heightPixels.times(height).toInt() }
                ?: WindowManager.LayoutParams.WRAP_CONTENT,
        )
    }

    val compositeDisposable = CompositeDisposable()

    open fun initViewProperties() = Unit

    open fun initListener() = Unit

    override fun show() {
        try {
            initListener()
            initViewProperties()
            if (!isShowing) super.show()
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) {
                toast(exception.message)
            } else {
                toast(string(R.string.error_base))
            }
        }
    }

    override fun dismiss() {
        try {
            compositeDisposable.clear()
            super.dismiss()
        } catch (exception: Exception) {
            if (BuildConfig.DEBUG) {
                toast(exception.message)
            } else {
                toast(string(R.string.error_base))
            }
        }
    }

}