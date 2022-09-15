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

package com.castcle.android.presentation.dialog.basic

import com.castcle.android.core.base.dialog.BaseDialog
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.DialogBasicBinding
import io.reactivex.rxkotlin.plusAssign

class BasicDialog(
    private val binding: DialogBasicBinding,
    private val button: String,
    isCancelable: Boolean = true,
    private val title: String,
    private val dismissAction: () -> Unit = {},
) : BaseDialog(binding = binding, isCancelable = isCancelable, width = 0.8) {

    override fun initViewProperties() {
        binding.tvTitle.text = title
        binding.tvButton.text = button
    }

    override fun initListener() {
        compositeDisposable += binding.tvButton.onClick {
            dismissAction()
            dismiss()
        }
    }

}