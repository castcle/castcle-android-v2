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

package com.castcle.android.data.tracker

import androidx.core.os.bundleOf
import com.auth0.android.jwt.JWT
import com.castcle.android.core.database.CastcleDatabase
import com.castcle.android.domain.tracker.TrackerRepository
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.core.annotation.Factory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Factory
class TrackerRepositoryImpl(
    private val database: CastcleDatabase,
) : TrackerRepository {

    private suspend fun getAccountId(): String {
        val accessToken = database.accessToken().get()?.accessToken.orEmpty()
        return try {
            JWT(accessToken).claims["id"]?.asString() ?: ""
        } catch (exception: Exception) {
            ""
        }
    }

    private suspend fun getAccountRole(): String {
        val accessToken = database.accessToken().get()?.accessToken.orEmpty()
        return try {
            JWT(accessToken).claims["role"]?.asString().orEmpty()
        } catch (exception: Exception) {
            ""
        }
    }

    private suspend fun sendToFirebase(name: String, parameter: Map<String, String>) {
        return suspendCoroutine { coroutine ->
            val bundle = bundleOf().apply {
                parameter.entries.forEach {
                    putString(it.key, it.value)
                }
            }
            coroutine.resume(Firebase.analytics.logEvent(name, bundle))
        }
    }

    override suspend fun trackAutoPostSyncSocial(active: Boolean, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "active" to if (active) "on" else "off",
            "user_id" to userId,
        )
        return sendToFirebase(name = "auto_post_sync_social", parameter = parameter)
    }

    override suspend fun trackConnectSyncSocial(channel: String, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "channel" to channel,
            "user_id" to userId,
        )
        return sendToFirebase(name = "connect_sync_social", parameter = parameter)
    }

    override suspend fun trackDeleteAccount(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "delete_account", parameter = parameter)
    }

    override suspend fun trackDisconnectSyncSocial(channel: String, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "channel" to channel,
            "user_id" to userId,
        )
        return sendToFirebase(name = "disconnect_sync_social", parameter = parameter)
    }

    override suspend fun trackLogin(channel: String, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "channel" to channel,
            "user_id" to userId,
        )
        return sendToFirebase(name = "login", parameter = parameter)
    }

    override suspend fun trackRegistration(channel: String, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "channel" to channel,
            "user_id" to userId,
        )
        return sendToFirebase(name = "registration", parameter = parameter)
    }

    override suspend fun trackResetPassword(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "reset_password", parameter = parameter)
    }

    override suspend fun trackVerificationMobile(countryCode: String, userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "country_code" to countryCode,
            "user_id" to userId,
        )
        return sendToFirebase(name = "verification_mobile", parameter = parameter)
    }

    override suspend fun trackViewAccount(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_account", parameter = parameter)
    }

    override suspend fun trackViewDeleteAccount(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_delete_account", parameter = parameter)
    }

    override suspend fun trackViewFeed(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "role" to getAccountRole(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_feed", parameter = parameter)
    }

    override suspend fun trackViewResetPassword(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_reset_password", parameter = parameter)
    }

    override suspend fun trackViewSetting(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_setting", parameter = parameter)
    }

    override suspend fun trackViewSyncSocial(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_sync_social", parameter = parameter)
    }

    override suspend fun trackViewWallet(userId: String) {
        val parameter = hashMapOf(
            "account_id" to getAccountId(),
            "user_id" to userId,
        )
        return sendToFirebase(name = "view_wallet", parameter = parameter)
    }

}