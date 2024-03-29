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

package com.castcle.android.presentation.dialog.recast

import androidx.lifecycle.MutableLiveData
import com.castcle.android.R
import com.castcle.android.core.base.recyclerview.CastcleViewEntity
import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.extensions.error
import com.castcle.android.core.extensions.loading
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.content.ContentRepository
import com.castcle.android.domain.content.entity.ParticipateEntity
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.UserType
import com.castcle.android.presentation.dialog.option.item_option_dialog.OptionDialogViewEntity
import com.castcle.android.presentation.dialog.recast.item_recast_title.RecastTitleViewEntity
import com.castcle.android.presentation.dialog.recast.item_select_recast_user.SelectRecastUserViewEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RecastDialogViewModel(
    private val contentId: String,
    private val contentRepository: ContentRepository,
    private val database: CastcleDatabase,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val currentUser = MutableStateFlow<List<Pair<Boolean, UserEntity>>?>(null)

    private val optionItems = currentUser.map { map ->
        map?.find { it.first }?.second?.toOptionItems()
    }

    private val participate = MutableStateFlow<List<Pair<String, ParticipateEntity>>>(listOf())

    val recastFail = MutableLiveData<Throwable>()

    val recastSuccess = MutableLiveData<Unit>()

    private val selectUserItems = MutableStateFlow<List<CastcleViewEntity>?>(null)

    private val viewStateItems = MutableStateFlow<List<CastcleViewEntity>?>(null)

    @FlowPreview
    val views = flowOf(optionItems, selectUserItems, viewStateItems)
        .flattenMerge()
        .filterNotNull()
        .distinctUntilChanged()

    init {
        getContentParticipate()
    }

    private fun getContentParticipate() {
        launch(
            onError = { viewStateItems.error(it) { getContentParticipate() } },
            onLaunch = { viewStateItems.loading() },
            onSuccess = { getCurrentUser() },
        ) {
            participate.value = contentRepository.getContentParticipate(contentId = contentId)
        }
    }

    private fun getCurrentUser() {
        launch {
            currentUser.value = database.user().get(UserType.People)
                .plus(database.user().get(UserType.Page))
                .map { Pair(it.type is UserType.People, it) }
        }
    }

    fun recast(isRecasted: Boolean) {
        launch(
            onError = { recastFail.postValue(it) },
            onSuccess = { recastSuccess.postValue(Unit) },
        ) {
            val selectedUserId = currentUser.value?.find { it.first }?.second?.id.orEmpty()
            if (isRecasted) {
                val otherUserRecasted = participate.value.any { (userId, it) ->
                    currentUser.value.orEmpty().map { it.second.id }.contains(userId)
                        && userId != selectedUserId
                        && it.recasted
                }
                userRepository.unrecastContent(
                    contentId = contentId,
                    otherUserRecasted = otherUserRecasted,
                    userId = selectedUserId,
                )
            } else {
                userRepository.recastContent(contentId = contentId, userId = selectedUserId)
            }
        }
    }

    fun showSelectUser() {
        selectUserItems.value = currentUser.value.orEmpty().map {
            SelectRecastUserViewEntity(
                isSelected = it.first,
                uniqueId = it.second.id,
                user = it.second,
            )
        }.let {
            listOf(RecastTitleViewEntity()).plus(it)
        }
    }

    fun updateSelectedUser(userId: String) {
        currentUser.value = currentUser.value.orEmpty().map {
            Pair(it.second.id == userId, it.second.copy(blocked = !it.second.blocked))
        }
    }

    private fun UserEntity.toOptionItems(): List<CastcleViewEntity> {
        val isRecasted = participate.value.find { find -> find.first == id }
            ?.second
            ?.recasted
            ?: false
        val selectedUserItem = RecastTitleViewEntity(
            user = this,
        )
        val recastItem = OptionDialogViewEntity(
            eventType = if (isRecasted) R.string.unrecast else R.string.recast,
            icon = R.drawable.ic_recast,
            title = if (isRecasted) R.string.unrecast else R.string.recast,
        )
        val quoteCastItem = OptionDialogViewEntity(
            eventType = R.string.quote_cast,
            icon = R.drawable.ic_quote_cast,
            title = R.string.quote_cast,
        )
        return listOf(selectedUserItem, recastItem, quoteCastItem)
    }

}