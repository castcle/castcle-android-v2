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

package com.castcle.android.presentation.setting.confirm_delete_account

import com.castcle.android.R
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.confirm_delete_account.item_confirm_delete_account.ConfirmDeleteAccountViewEntity
import com.castcle.android.presentation.setting.confirm_delete_account.item_confirm_delete_page.ConfirmDeletePageViewEntity
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class ConfirmDeleteAccountViewModel(
    database: CastcleDatabase,
    userId: String,
) : BaseViewModel() {

    val title = database.user().retrieve(userId)
        .filterNotNull()
        .map {
            if (it.type is UserType.People) {
                R.string.delete_account
            } else {
                R.string.delete_page
            }
        }

    val views = database.user().retrieve(userId)
        .filterNotNull()
        .map {
            if (it.type is UserType.People) {
                ConfirmDeleteAccountViewEntity(user = it)
            } else {
                ConfirmDeletePageViewEntity(user = it)
            }
        }

}