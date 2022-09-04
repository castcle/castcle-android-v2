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

package com.castcle.android.core.base.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.castcle.android.R
import com.castcle.android.core.base.activity.BaseActivity
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.hideKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import io.reactivex.disposables.CompositeDisposable

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    val compositeDisposable = CompositeDisposable()

    fun backPress() {
        findNavController().popBackStack()
        hideKeyboard()
    }

    open fun initConsumer() = Unit

    open fun initListener() = Unit

    open fun initObserver() = Unit

    open fun initViewProperties() = Unit

    fun NavDirections.navigate() {
        findNavController().navigate(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.BottomSheetDialogTheme)
        initConsumer()
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewProperties()
        initListener()
        initObserver()
    }

    fun showLoading() {
        activity?.cast<BaseActivity>()?.showLoading()
    }

    fun dismissLoading() {
        activity?.cast<BaseActivity>()?.dismissLoading()
    }

}