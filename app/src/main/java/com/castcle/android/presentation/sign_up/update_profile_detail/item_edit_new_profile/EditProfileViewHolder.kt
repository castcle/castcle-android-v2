package com.castcle.android.presentation.sign_up.update_profile_detail.item_edit_new_profile

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.setStatePass
import com.castcle.android.databinding.ItemEditDetailNewProfileBinding
import com.castcle.android.presentation.sign_up.update_profile_detail.EditNewProfileListener
import io.reactivex.disposables.CompositeDisposable

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
//  Created by sklim on 7/9/2022 AD at 11:47.

class EditProfileViewHolder(
    val binding: ItemEditDetailNewProfileBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: EditNewProfileListener,
) : CastcleViewHolder<EditProfileViewEntity>(binding.root) {

    override var item = EditProfileViewEntity()

    init {

    }

    private fun handleButtonDone(notBlank: Boolean) {
        binding.btDone.run {
            isEnabled = notBlank
            setStatePass(notBlank)
        }
    }

    override fun bind(bindItem: EditProfileViewEntity) {
        super.bind(bindItem)

    }
}