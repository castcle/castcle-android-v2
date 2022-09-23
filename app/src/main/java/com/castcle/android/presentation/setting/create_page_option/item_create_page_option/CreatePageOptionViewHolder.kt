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

package com.castcle.android.presentation.setting.create_page_option.item_create_page_option

import com.castcle.android.core.base.recyclerview.CastcleViewHolder
import com.castcle.android.core.extensions.onClick
import com.castcle.android.databinding.ItemCreatePageOptionBinding
import com.castcle.android.presentation.setting.create_page_option.CreatePageOptionListener
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

class CreatePageOptionViewHolder(
    binding: ItemCreatePageOptionBinding,
    private val compositeDisposable: CompositeDisposable,
    private val listener: CreatePageOptionListener
) : CastcleViewHolder<CreatePageOptionViewEntity>(binding.root) {

    override var item = CreatePageOptionViewEntity()

    init {
        compositeDisposable += binding.tvCreatePageWithFacebook.onClick {
            listener.onCreatePageWithFacebookClicked()
        }
        compositeDisposable += binding.tvCreatePageWithTwitter.onClick {
            listener.onCreatePageWithTwitterClicked()
        }
        compositeDisposable += binding.tvCreatePage.onClick {
            listener.onCreatePageClicked()
        }
    }

}