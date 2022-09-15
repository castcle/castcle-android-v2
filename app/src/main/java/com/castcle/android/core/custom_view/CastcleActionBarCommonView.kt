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
import com.castcle.android.R
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ToolbarCastcleCommonBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class CastcleActionBarCommonView(context: Context, attrs: AttributeSet) :
    ConstraintLayout(context, attrs) {

    private val binding = ToolbarCastcleCommonBinding.inflate(
        LayoutInflater.from(context), this, true
    )

    private val compositeDisposable = CompositeDisposable()

    fun bind(
        leftButtonAction: (() -> Unit)? = null,
        @DrawableRes leftButtonIcon: Int? = R.drawable.ic_back,
        rightButtonAction: (() -> Unit)? = null,
        rightMessageAction: Any? = "",
        title: Any? = "",
        @ColorRes titleColor: Int = R.color.white,
        @ColorRes rightMessageColor: Int = R.color.blue,
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

        if (rightButtonAction != null && rightMessageAction != null) {
            compositeDisposable += binding.tvRightMessage.onClick { rightButtonAction.invoke() }
        }

        binding.tvRightMessage.visibleOrGone(rightButtonAction != null)
        binding.ivLeftIcon.visibleOrGone(leftButtonAction != null)
        binding.tvTitle.setTextColor(context.getColor(titleColor))
        binding.tvRightMessage.text = when (rightMessageAction) {
            is String -> rightMessageAction
            is Int -> try {
                context.getString(rightMessageAction)
            } catch (exception: Exception) {
                ""
            }
            else -> ""
        }

        binding.tvRightMessage.setTextColor(context.getColor(rightMessageColor))
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