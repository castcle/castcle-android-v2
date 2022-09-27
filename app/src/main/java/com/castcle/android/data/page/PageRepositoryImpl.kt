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

package com.castcle.android.data.page

import androidx.room.withTransaction
import com.castcle.android.core.api.PageApi
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.extensions.getLargeProfileImageUrlHttps
import com.castcle.android.core.glide.GlidePreloader
import com.castcle.android.data.page.entity.SyncSocialRequest
import com.castcle.android.data.user.entity.DeleteAccountRequest
import com.castcle.android.domain.page.PageRepository
import com.castcle.android.domain.user.entity.SyncSocialEntity
import com.castcle.android.domain.user.entity.UserEntity
import com.castcle.android.domain.user.type.SocialType
import com.twitter.sdk.android.core.*
import com.twitter.sdk.android.core.models.User
import org.koin.core.annotation.Factory
import kotlin.coroutines.*

@Factory
class PageRepositoryImpl(
    private val api: PageApi,
    private val database: CastcleDatabase,
    private val glidePreloader: GlidePreloader,
) : PageRepository {

    override suspend fun createPageWithFacebook(body: SyncSocialRequest) {
        val response = apiCall { api.createPageWithSocial(body = body) }
        val user = UserEntity.mapOwner(response?.payload)
        val syncSocialPage = SyncSocialEntity.map(response?.payload)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.syncSocial().insert(syncSocialPage)
            database.user().upsert(user)
        }
    }

    override suspend fun createPageWithTwitter(token: TwitterAuthToken?) {
        val body = suspendCoroutine<SyncSocialRequest> { coroutine ->
            TwitterCore.getInstance().apiClient.accountService
                .verifyCredentials(false, true, true)
                .enqueue(object : Callback<User>() {
                    override fun failure(exception: TwitterException?) {
                        coroutine.resumeWithException(Throwable(exception?.message))
                    }

                    override fun success(result: Result<User>?) {
                        val body = SyncSocialRequest(
                            authToken = "${token?.token}",
                            avatar = result?.data?.getLargeProfileImageUrlHttps(),
                            cover = result?.data?.profileBannerUrl,
                            displayName = result?.data?.name,
                            link = result?.data?.idStr?.let { "https://twitter.com/i/user/$it" },
                            overview = result?.data?.description,
                            provider = SocialType.Twitter.id,
                            socialId = result?.data?.idStr,
                            userName = result?.data?.screenName,
                        )
                        coroutine.resume(body)
                    }
                })
        }
        val response = apiCall { api.createPageWithSocial(body = body) }
        val user = UserEntity.mapOwner(response?.payload)
        val syncSocialPage = SyncSocialEntity.map(response?.payload)
        glidePreloader.loadUser(user)
        database.withTransaction {
            database.syncSocial().insert(syncSocialPage)
            database.user().upsert(user)
        }
    }

    override suspend fun deletePage(body: DeleteAccountRequest, userId: String) {
        apiCall { api.deletePage(body = body, id = userId) }
        database.withTransaction {
            database.syncSocial().deleteByUserId(listOf(userId))
            database.user().delete(userId)
        }
    }

}