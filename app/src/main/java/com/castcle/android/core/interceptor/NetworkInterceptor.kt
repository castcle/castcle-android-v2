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

import com.castcle.android.core.constants.*
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.core.extensions.toBearer
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.koin.core.annotation.Singleton
import java.util.*

@Singleton
class NetworkInterceptor(private val database: CastcleDatabase) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(chain.request().addHeader())
    }

    private fun Request.addHeader(): Request {
        return withAccessToken()
            .addHeader(HEADER_ACCEPT_LANGUAGE, Locale.getDefault().language)
            .addHeader(HEADER_API_META_DATA, "src=android,dest=castcle")
            .addHeader(HEADER_CONTENT_TYPE, "application/json")
            .build()
    }

    private fun Request.withAccessToken(): Request.Builder {
        val accessToken = runBlocking { database.accessToken().get() }?.accessToken
        val isRequestContainAccessToken = !headers[HEADER_AUTHORIZATION].isNullOrBlank()
        return if (accessToken.isNullOrBlank() || isRequestContainAccessToken) {
            newBuilder()
        } else {
            newBuilder().addHeader(HEADER_AUTHORIZATION, accessToken.toBearer(url))
        }
    }

}