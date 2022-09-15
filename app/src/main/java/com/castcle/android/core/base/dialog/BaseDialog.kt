/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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