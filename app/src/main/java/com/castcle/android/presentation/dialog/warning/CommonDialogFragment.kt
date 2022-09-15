package com.castcle.android.presentation.dialog.warning

import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.LayoutCommontWarningValueBinding
import com.castcle.android.presentation.dialog.warning.entity.CommonWarningBase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

//  Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
//  DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
//
//  This code is free software; you can redistribute it and/or modify it
//  under the terms of the GNU General Public License version 3 only, as
//  published by the Free Software Foundation.
//
//  This code is distributed in the hope that it will be useful, but WITHOUT
//  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
//  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
//  version 3 for more details (a copy is included in the LICENSE file that
//  accompanied this code).
//
//  You should have received a copy of the GNU General Public License version
//  3 along with this work; if not, write to the Free Software Foundation,
//  Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
//
//  Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
//  Thailand 10160, or visit www.castcle.com if you need additional information
//  or have any questions.
//
//
//  Created by sklim on 29/3/2022 AD at 10:57.

class CommonDialogFragment(
    private val commonWarningBase: CommonWarningBase,
    private val onClose: (() -> Unit)?,
) : DialogFragment() {

    val compositeDisposable = CompositeDisposable()

    private val binding by lazy {
        LayoutCommontWarningValueBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.apply {
            setStyle(STYLE_NORMAL, R.style.Dialog_NoTitle)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
        }
        return dialog
    }

    override fun onStart() {
        super.onStart()
        with(binding) {
            when (commonWarningBase) {
                is CommonWarningBase.WarningUiModel -> {
                    tvWarningTitle.run {
                        text = commonWarningBase.titleWarning
                        setColorWaring(requireContext(), true)
                    }
                    tvWarningDescription.run {
                        setColorWaring(requireContext(), true)
                        text = commonWarningBase.warningDescription
                    }
                    btClose.isActivated = true
                }
                is CommonWarningBase.SuccessUiModel -> {
                    tvWarningTitle.gone()
                    tvWarningDescription.text = commonWarningBase.message
                }
                is CommonWarningBase.ReviewError -> {
                    handleBindErrorList(commonWarningBase)
                }
                else -> Unit
            }

            compositeDisposable += btClose.onClick {
                dismiss()
                onClose?.invoke()
            }
        }
    }

    private fun handleBindErrorList(commonWarningBase: CommonWarningBase.ReviewError) {
        with(binding) {
            tvWarningTitle.run {
                text = commonWarningBase.titleWarning
                setColorWaring(requireContext(), true)
            }
            liErrorList.visible()
            tvWarningDescription.gone()
            btClose.isActivated = true
            commonWarningBase.errorList.forEach {
                liErrorList.addView(getTextView(it))
            }
        }
    }

    private fun getTextView(message: String): TextView {
        return TextView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 20, 0, 0)
                setPadding(10, 10, 0, 10)
            }
            text = message
            setTextColor(requireContext().getColorResource(R.color.red_3))
            textSize = 16.0f
        }
    }

    companion object {
        fun newInstance(
            commonWarningBase: CommonWarningBase,
            onClose: (() -> Unit)? = null
        ) = CommonDialogFragment(
            commonWarningBase,
            onClose
        )
    }
}
