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

package com.castcle.android.domain.tracker

interface TrackerRepository {
    suspend fun trackAutoPostSyncSocial(active: Boolean, userId: String)
    suspend fun trackConnectSyncSocial(channel: String, userId: String)
    suspend fun trackDeleteAccount(userId: String)
    suspend fun trackDisconnectSyncSocial(channel: String, userId: String)
    suspend fun trackLogin(channel: String, userId: String)
    suspend fun trackRegistration(channel: String, userId: String)
    suspend fun trackResetPassword(userId: String)
    suspend fun trackVerificationMobile(countryCode: String, userId: String)
    suspend fun trackViewAccount(userId: String)
    suspend fun trackViewDeleteAccount(userId: String)
    suspend fun trackViewFeed(userId: String)
    suspend fun trackViewResetPassword(userId: String)
    suspend fun trackViewSetting(userId: String)
    suspend fun trackViewSyncSocial(userId: String)
    suspend fun trackViewWallet(userId: String)
}