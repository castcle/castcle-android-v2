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

package com.castcle.android.domain.authentication

import com.castcle.android.data.authentication.entity.*
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import com.castcle.android.domain.authentication.entity.OtpEntity
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.twitter.sdk.android.core.TwitterAuthToken

interface AuthenticationRepository {
    suspend fun changePassword(otp: OtpEntity)
    suspend fun getAccessToken(): AccessTokenEntity
    suspend fun getFirebaseMessagingToken(): String
    suspend fun fetchGuestAccessToken()
    suspend fun linkWithFacebook()
    suspend fun linkWithSocial(body: LoginWithSocialRequest)
    suspend fun linkWithTwitter(token: TwitterAuthToken?)
    suspend fun loginWithEmail(body: LoginWithEmailRequest)
    suspend fun loginWithFacebook()
    suspend fun loginWithGoogle(signInAccount: GoogleSignInAccount)
    suspend fun loginWithSocial(body: LoginWithSocialRequest)
    suspend fun loginWithTwitter(token: TwitterAuthToken?)
    suspend fun loginOut()
    suspend fun loginOutFacebook()
    suspend fun registerFirebaseMessagingToken()
    suspend fun requestOtp(otp: OtpEntity): OtpEntity
    suspend fun resentVerifyEmail()
    suspend fun unregisterFirebaseMessagingToken()
    suspend fun updateMobileNumber(otp: OtpEntity): OtpEntity
    suspend fun verifyOtp(otp: OtpEntity): OtpEntity
}