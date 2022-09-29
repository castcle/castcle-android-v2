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

package com.castcle.android.presentation.setting.delete_account.item_delete_account

import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleAdapter
import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.*
import com.castcle.android.databinding.ItemDeleteAccountBinding
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.delete_account.DeleteAccountListener
import com.castcle.android.presentation.setting.delete_account.item_delete_account_user.DeleteAccountUserViewRenderer
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class DeleteAccountViewHolder(
    private val binding: ItemDeleteAccountBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: DeleteAccountListener,
) : CastcleViewHolder<DeleteAccountViewEntity>(binding.root) {

    override var item = DeleteAccountViewEntity()

    private val adapter by lazy {
        CastcleAdapter(listener, compositeDisposable).apply {
            registerRenderer(DeleteAccountUserViewRenderer())
        }
    }

    init {
        binding.recyclerView.adapter = adapter
        compositeDisposable += binding.tvConfirm.onClick {
            listener.onDeleteAccountClicked(item.password, item.type)
        }
        compositeDisposable += binding.etPassword.onTextChange {
            item.password = it
            updateConfirmButton()
        }
    }

    override fun bind(bindItem: DeleteAccountViewEntity) {
        updateConfirmButton()
        adapter.submitList(item.user)
        binding.etPassword.setText(item.password)
        binding.tvTitle1.text = context().getString(
            R.string.fragment_delete_account_title_1,
            when (item.type) {
                is UserType.Page -> string(R.string.page)
                is UserType.People -> string(R.string.profile)
            },
        )
        binding.tvTitle2.text = context().getString(
            R.string.fragment_delete_account_title_2,
            when (item.type) {
                is UserType.Page -> string(R.string.page)
                is UserType.People -> string(R.string.profile)
            },
        )
    }

    private fun updateConfirmButton() {
        val isEnable = item.password.isNotBlank()
        binding.tvConfirm.isEnabled = isEnable
        binding.tvConfirm.backgroundTintList = if (isEnable) {
            colorStateList(R.color.blue)
        } else {
            colorStateList(R.color.gray_1)
        }
    }

}