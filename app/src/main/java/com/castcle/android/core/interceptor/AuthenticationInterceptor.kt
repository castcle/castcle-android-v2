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

package com.castcle.android.core.interceptor

import com.castcle.android.core.api.AuthenticationApiHolder
import com.castcle.android.core.constants.HEADER_AUTHORIZATION
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.toBearer
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.authentication.entity.RecursiveRefreshTokenEntity
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.koin.core.annotation.Singleton
import java.util.concurrent.atomic.*

@Singleton
class AuthenticationInterceptor(
    private val apiHolder: AuthenticationApiHolder,
    private val database: CastcleDatabase,
) : Authenticator {

    private val refreshing = AtomicBoolean(false)

    override fun authenticate(route: Route?, response: Response): Request? {
        return when {
            refreshing.get() -> null
            response.retryCount() > 0 -> triggerRecursiveRefreshToken()
            else -> requestNewAccessToken(response)
        }
    }

    private fun requestNewAccessToken(response: Response): Request {
        refreshing.set(true)
        val api = apiHolder.api ?: return response.toNewRequest()
        val token = runBlocking { database.accessToken().get() } ?: return response.toNewRequest()
        val refreshTokenResponse = runBlocking { api.refreshToken(token.refreshToken.toBearer()) }
        return if (refreshTokenResponse.isSuccessful) {
            response.toNewRequest(AccessTokenEntity.map(refreshTokenResponse.body(), token.type))
        } else {
            response.toNewRequest()
        }
    }

    private fun Response.retryCount(count: Int = 0): Int {
        return priorResponse?.retryCount(count + 1) ?: count
    }

    private fun Response.toNewRequest(token: AccessTokenEntity? = null): Request {
        refreshing.set(false)
        val accessTokenWithPrefix = token?.accessToken
            ?.ifBlank { null }
            ?.toBearer()
        return if (accessTokenWithPrefix != null) {
            runBlocking { database.accessToken().insert(token) }
            request.newBuilder().header(HEADER_AUTHORIZATION, accessTokenWithPrefix).build()
        } else {
            request.newBuilder().build()
        }
    }

    private fun triggerRecursiveRefreshToken(): Request? {
        runBlocking {
            database.recursiveRefreshToken().insert(RecursiveRefreshTokenEntity())
        }
        return null
    }

}