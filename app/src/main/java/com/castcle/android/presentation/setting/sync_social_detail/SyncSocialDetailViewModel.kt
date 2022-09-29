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

package com.castcle.android.presentation.setting.sync_social_detail

import com.castcle.android.core.base.view_model.BaseViewModel
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.tracker.TrackerRepository
import com.castcle.android.domain.user.UserRepository
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.SocialType
import com.castcle.android.presentation.setting.sync_social_detail.item_sync_social_detail.SyncSocialDetailViewEntity
import kotlinx.coroutines.flow.*
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class SyncSocialDetailViewModel(
    database: CastcleDatabase,
    private val synSocialId: String,
    private val trackerRepository: TrackerRepository,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    val onDisconnect = MutableSharedFlow<Unit>()

    val onError = MutableSharedFlow<Throwable>()

    val onSuccess = MutableSharedFlow<Unit>()

    val syncSocialType = MutableStateFlow<SocialType?>(null)

    val views = database.syncSocial().retrieveWithUser(id = synSocialId).filterNotNull().onEach {
        syncSocialType.value = it.syncSocial.provider
    }.map {
        SyncSocialDetailViewEntity(
            syncSocial = it.syncSocial,
            user = it.user ?: UserEntity(),
        )
    }

    fun disconnectWithSocial(userId: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = { onDisconnect.emitOnSuspend() },
        ) {
            userRepository.disconnectWithSocial(
                syncSocialId = synSocialId,
                userId = userId,
            )
        }
        launch {
            trackerRepository.trackDisconnectSyncSocial(
                channel = syncSocialType.value?.id.orEmpty(),
                userId = userId,
            )
        }
    }

    fun updateAutoPost(enable: Boolean, userId: String) {
        launch(
            onError = { onError.emitOnSuspend(it) },
            onSuccess = {
                onSuccess.emitOnSuspend()
                launch { trackerRepository.trackAutoPostSyncSocial(enable, userId) }
            },
        ) {
            userRepository.updateAutoPost(
                enable = enable,
                syncSocialId = synSocialId,
                userId = userId,
            )
        }
    }

}