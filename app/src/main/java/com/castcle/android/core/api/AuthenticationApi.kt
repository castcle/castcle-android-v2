package com.castcle.android.core.api

import com.castcle.android.core.constants.HEADER_AUTHORIZATION
import com.castcle.android.data.authentication.entity.*
import retrofit2.Response
import retrofit2.http.*

interface AuthenticationApi {

    @POST("v2/authentications/guest")
    suspend fun getGuestAccessToken(
        @Body body: GetGuestAccessTokenRequest,
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

    @POST("v2/authentications/request-link/email")
    suspend fun resentVerifyEmail(): Response<Unit>

    @HTTP(method = "DELETE", path = "/v2/authentications/register/notification", hasBody = true)
    suspend fun unregisterFirebaseMessagingToken(
        @Body body: RegisterFirebaseMessagingTokenRequest
    ): Response<Unit>

}