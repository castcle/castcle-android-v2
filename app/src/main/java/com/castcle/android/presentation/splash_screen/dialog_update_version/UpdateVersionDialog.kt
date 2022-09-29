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

package com.castcle.android.presentation.splash_screen.dialog_update_version

import android.app.AlertDialog
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.castcle.android.R
import com.castcle.android.core.extensions.cast
import com.castcle.android.core.extensions.color
import com.castcle.android.domain.setting.entity.UpdateVersionEntity

class UpdateVersionDialog(
    private val onNegativeButtonClick: () -> Unit,
    private val onPositiveButtonClick: () -> Unit,
    private val updateVersion: UpdateVersionEntity,
) : DialogFragment() {

    private val negativeButton by lazy {
        dialog?.cast<AlertDialog>()?.getButton(AlertDialog.BUTTON_NEGATIVE)
    }

    private val positiveButton by lazy {
        dialog?.cast<AlertDialog>()?.getButton(AlertDialog.BUTTON_POSITIVE)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog =
        AlertDialog.Builder(requireContext()).apply {
            setNegativeButton(updateVersion.buttonNegativeMessage, null)
            setPositiveButton(updateVersion.buttonPositiveMessage, null)
            setMessage(updateVersion.message)
            setTitle(updateVersion.title)
        }.create()

    override fun onStart() {
        super.onStart()
        isCancelable = false
        negativeButton?.isVisible = updateVersion.buttonNegativeMessage.isNotBlank()
        negativeButton?.setTextColor(color(R.color.blue))
        negativeButton?.setOnClickListener {
            onNegativeButtonClick()
            dismiss()
        }
        positiveButton?.setTextColor(color(R.color.blue))
        positiveButton?.setOnClickListener {
            onPositiveButtonClick()
        }
    }

}