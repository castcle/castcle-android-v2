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

package com.castcle.android.presentation.setting.account.item_menu

import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemAccountMenuBinding
import com.castcle.android.presentation.setting.account.AccountListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class AccountMenuViewHolder(
    private val binding: ItemAccountMenuBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: AccountListener,
) : CastcleViewHolder<AccountMenuViewEntity>(binding.root) {

    override var item = AccountMenuViewEntity()

    init {
        compositeDisposable += binding.root.onClick {
            item.action(listener)
        }
    }

    override fun bind(bindItem: AccountMenuViewEntity) {
        binding.ivArrow.isInvisible = !item.showArrow
        binding.tvTitle.text = string(item.titleId)
        binding.ivImage.setImageResource(item.imageIcon ?: R.drawable.ic_facebook)
        binding.ivImage.backgroundTintList = colorStateList(item.imageColor)
        binding.ivImage.isVisible = item.imageIcon != null
        binding.tvDescription.isVisible = item.description != null
        binding.tvDescription.text = when (val description = item.description) {
            is Int -> string(description)
            is String -> description
            else -> ""
        }
        binding.tvDetail.setTextColor(color(item.detailColor))
        binding.tvDetail.text = when (val detail = item.detail) {
            is Int -> string(detail)
            is String -> detail
            else -> ""
        }
    }

}