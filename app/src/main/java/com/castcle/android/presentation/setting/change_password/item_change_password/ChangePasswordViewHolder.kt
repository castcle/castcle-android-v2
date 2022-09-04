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

package com.castcle.android.presentation.setting.change_password.item_change_password

import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemChangePasswordBinding
import com.castcle.android.presentation.setting.change_password.ChangePasswordListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import java.util.regex.*

class ChangePasswordViewHolder(
    private val binding: ItemChangePasswordBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: ChangePasswordListener,
) : CastcleViewHolder<ChangePasswordViewEntity>(binding.root) {

    override var item = ChangePasswordViewEntity()

    private val regex = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z]).*$").toRegex()

    init {
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onChangePassword(item.password)
        }
        compositeDisposable += binding.etPassword.onTextChange {
            item.password = it
            updateView()
        }
        compositeDisposable += binding.etConfirmPassword.onTextChange {
            item.confirmPassword = it
            updateView()
        }
    }

    override fun bind(bindItem: ChangePasswordViewEntity) {
        updateView()
        binding.etPassword.setText(item.password)
        binding.etConfirmPassword.setText(item.confirmPassword)
    }

    private fun updateView() {
        val password = binding.etPassword.text.toString()
        val confirm = binding.etConfirmPassword.text.toString()
        val checkCase = password.matches(regex)
        val checkLength = password.length >= 6
        val checkMatch = confirm.isNotBlank() && password == confirm
        binding.tvConfirm.isEnabled = checkCase && checkLength && checkMatch
        binding.tvConfirm.backgroundTintList = if (binding.tvConfirm.isEnabled) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
        updateConditionView(
            condition = checkCase,
            emptyState = password.isBlank(),
            imageView = binding.ivCheckCase,
            textView = binding.tvCheckCase,
        )
        updateConditionView(
            condition = checkLength,
            emptyState = password.isBlank(),
            imageView = binding.ivCheckLength,
            textView = binding.tvCheckLength,
        )
        updateConditionView(
            condition = checkMatch,
            emptyState = confirm.isBlank(),
            imageView = binding.ivCheckMatch,
            textView = binding.tvCheckMatch,
        )
    }

    private fun updateConditionView(
        condition: Boolean,
        emptyState: Boolean,
        imageView: AppCompatImageView,
        textView: AppCompatTextView,
    ) {
        val color = when {
            emptyState -> colorStateList(R.color.gray_1)
            condition -> colorStateList(R.color.blue)
            else -> colorStateList(R.color.red_3)
        }
        textView.setTextColor(color)
        imageView.imageTintList = color
        imageView.setImageResource(
            if (emptyState || condition) {
                R.drawable.ic_check_circle
            } else {
                R.drawable.ic_close
            }
        )
    }

}