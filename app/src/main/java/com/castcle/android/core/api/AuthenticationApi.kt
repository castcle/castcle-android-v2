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

package com.castcle.android.core.api

import android.os.Build
import com.castcle.android.core.constants.*
import com.castcle.android.data.authentication.entity.*
import retrofit2.Response
import retrofit2.http.*

interface AuthenticationApi {

    @POST("v2/authentications/change-password")
    suspend fun changePassword(
        @Body body: ChangePasswordRequest,
    ): Response<Unit>

    @POST("v2/authentications/guest")
    suspend fun getGuestAccessToken(
        @Body body: GetGuestAccessTokenRequest,
        @Header(HEADER_DEVICE) device: String = Build.MODEL,
        @Header(HEADER_PLATFORM) platform: String = "Android ${Build.VERSION.RELEASE}",
    ): Response<GetGuestAccessTokenResponse>

    @POST("v2/authentications/connect-with-social")
    suspend fun linkWithSocial(
        @Body body: LoginWithSocialRequest,
    ): Response<LoginResponse>

    @POST("v2/authentications/login-with-email")
    suspend fun loginWithEmail(
        @Body body: LoginWithEmailRequest,
    ): Response<LoginResponse>

    @POST("v2/authentications/login-with-social")
    suspend fun loginWithSocial(
        @Body body: LoginWithSocialRequest,
    ): Response<LoginResponse>

    @POST("v2/authentications/refresh-token")
    suspend fun refreshToken(
        @Header(HEADER_AUTHORIZATION) refreshToken: String,
    ): Response<LoginResponse>

    @POST("v2/authentications/register/notification")
    suspend fun registerFirebaseMessagingToken(
        @Body body: RegisterFirebaseMessagingTokenRequest
    ): Response<Unit>

    @POST("v2/authentications/request-otp/email")
    suspend fun requestOtpEmail(
        @Body body: RequestOtpRequest,
    ): Response<RequestOtpResponse>

    @POST("v2/authentications/request-otp/mobile")
    suspend fun requestOtpMobile(
        @Body body: RequestOtpRequest,
    ): Response<RequestOtpResponse>

    @POST("v2/authentications/request-link/email")
    suspend fun resentVerifyEmail(): Response<Unit>

    @HTTP(method = "DELETE", path = "/v2/authentications/register/notification", hasBody = true)
    suspend fun unregisterFirebaseMessagingToken(
        @Body body: RegisterFirebaseMessagingTokenRequest
    ): Response<Unit>

    @PUT("v2/users/me/mobile")
    suspend fun updateMobileNumber(
        @Body body: UpdateMobileNumberRequest,
    ): Response<Unit>

    @POST("v2/authentications/verify-otp/email")
    suspend fun verifyOtpEmail(
        @Body body: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("v2/authentications/verify-otp/mobile")
    suspend fun verifyOtpMobile(
        @Body body: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

    @POST("v2/authentications/verify-password")
    suspend fun verifyPassword(
        @Body body: VerifyOtpRequest
    ): Response<VerifyOtpResponse>

}