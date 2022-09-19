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

package com.castcle.android.presentation.setting.delete_account

import com.castcle.android.R
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.data.user.entity.DeleteAccountRequest
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.setting.delete_account.item_delete_account.DeleteAccountViewEntity
import com.castcle.android.presentation.setting.delete_account.item_delete_account_user.DeleteAccountUserViewEntity
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class DeleteAccountViewModel(
    database: CastcleDatabase,
    private val repository: UserRepository,
    private val userId: String,
) : BaseViewModel() {

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<UserType>()

    val title = database.user().retrieve(userId).filterNotNull().map {
        if (it.type is UserType.People) {
            R.string.delete_account
        } else {
            R.string.delete_page
        }
    }

    val views = database.user().retrieve(UserType.People)
        .combine(database.user().retrieve(UserType.Page)) { user, page -> user.plus(page) }
        .map { user ->
            val type = user.find { it.id == userId }?.type ?: UserType.People
            val userItems = if (type is UserType.People) {
                user
            } else {
                user.filter { it.id == userId }
            }.map { map ->
                DeleteAccountUserViewEntity(user = map, uniqueId = map.id)
            }
            DeleteAccountViewEntity(
                type = type,
                user = userItems,
            )
        }

    fun deleteAccount(password: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(UserType.People) },
        ) {
            repository.deleteAccount(body = DeleteAccountRequest(password = password))
        }
    }

    fun deletePage(password: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onSuccess.emitOnSuspend(UserType.Page) },
        ) {
            repository.deletePage(body = DeleteAccountRequest(password = password), userId = userId)
        }
    }

}