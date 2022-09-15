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

package com.castcle.android.core.custom_view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.extensions.onClick
import com.castcle.android.core.extensions.visible
import com.castcle.android.databinding.LayoutCastcleActionBarBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class CastcleActionBarView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private val binding = LayoutCastcleActionBarBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    fun bind(
        leftButtonAction: (() -> Unit)? = null,
        @DrawableRes leftButtonIcon: Int? = R.drawable.ic_back,
        rightTextButtonAction: (() -> Unit)? = null,
        rightTextButtonMessage: Any? = null,
        rightButtonAction: (() -> Unit)? = null,
        @DrawableRes rightButtonIcon: Int? = null,
        rightSecondButtonAction: (() -> Unit)? = null,
        @DrawableRes rightSecondButtonIcon: Int? = null,
        title: Any? = "",
        @ColorRes titleColor: Int = R.color.white,
    ) {
        compositeDisposable.clear()
        if (leftButtonAction != null && leftButtonIcon != null) {
            compositeDisposable += binding.ivLeftIcon.onClick { leftButtonAction.invoke() }
        }
        if (leftButtonIcon != null) {
            binding.ivLeftIcon.setImageResource(leftButtonIcon)
        } else {
            binding.ivLeftIcon.setImageDrawable(null)
        }
        if (rightButtonAction != null && rightButtonIcon != null) {
            compositeDisposable += binding.ivRightIcon.onClick { rightButtonAction.invoke() }
        } else {
            binding.ivRightIcon.setOnClickListener(null)
        }
        if (rightTextButtonAction != null && rightTextButtonMessage != null) {
            compositeDisposable += binding.ivRightTextIcon.onClick { rightTextButtonAction.invoke() }
        } else {
            binding.ivRightTextIcon.setOnClickListener(null)
        }
        if (rightButtonIcon != null) {
            binding.ivRightIcon.setImageResource(rightButtonIcon)
        } else {
            binding.ivRightIcon.setImageDrawable(null)
        }
        if (rightSecondButtonAction != null && rightSecondButtonIcon != null) {
            compositeDisposable += binding.ivRightSecondIcon.onClick { rightSecondButtonAction.invoke() }
        } else {
            binding.ivRightSecondIcon.setOnClickListener(null)
        }
        if (rightSecondButtonIcon != null) {
            binding.ivRightSecondIcon.setImageResource(rightSecondButtonIcon)
        } else {
            binding.ivRightSecondIcon.setImageDrawable(null)
        }
        binding.ivLeftIcon.isVisible = leftButtonIcon != null
        binding.ivRightIcon.isVisible = rightButtonIcon != null
        binding.ivRightTextIcon.isVisible = rightTextButtonMessage != null
        binding.ivRightTextIcon.text = when (rightTextButtonMessage) {
            is String -> rightTextButtonMessage
            is Int -> try {
                context.getString(rightTextButtonMessage)
            } catch (exception: Exception) {
                ""
            }
            else -> ""
        }
        binding.ivRightSecondIcon.isVisible = rightSecondButtonIcon != null
        binding.tvTitle.setTextColor(context.getColor(titleColor))
        binding.tvTitle.text = when (title) {
            is String -> title
            is Int -> try {
                context.getString(title)
            } catch (exception: Exception) {
                ""
            }
            else -> ""
        }
        visible()
    }

}