package com.castcle.android.core.interceptor

import android.content.Context
import android.os.Build
import com.castcle.android.BuildConfig
import com.castcle.android.core.constants.*
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.extensions.getDeviceUniqueId
import com.castcle.android.core.storage.app_config.AppConfig
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.data.authentication.entity.GetGuestAccessTokenRequest
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import kotlinx.coroutines.runBlocking
import okhttp3.*
import org.koin.core.annotation.Singleton
import timber.log.Timber

@Singleton
class NetworkInterceptor(
    private val appConfig: AppConfig,
    private val database: CastcleDatabase,
    private val context: Context,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithHeaders = chain.request().addHeader()
        val response = chain.proceed(requestWithHeaders)
        if (response.code == 401) {
            val api = appConfig.api ?: return response
            val oldToken = runBlocking { database.accessToken().get().firstOrNull() }
            val newToken = runBlocking {
                val tokenResponse = apiCall { api.getGuestAccessToken(body = newTokenRequest()) }
                val token = AccessTokenEntity.map(tokenResponse)
                database.accessToken().insert(token)
                token
            }
            return if (oldToken?.isGuest() == true) {
                Timber.e(response.body?.string())
                val newRequest = response.request.newBuilder()
                    .header(HEADER_AUTHORIZATION, "$AUTHORIZATION_PREFIX${newToken.accessToken}")
                    .build()
                chain.proceed(newRequest)
            } else {
                response
            }
        }
        return response
    }

    private fun newTokenRequest(): GetGuestAccessTokenRequest {
        return GetGuestAccessTokenRequest(context.getDeviceUniqueId())
    }

    private fun Request.addHeader(): Request {
        return buildWithAccessToken()
            .addHeader(HEADER_ACCEPT_LANGUAGE, "en")
            .addHeader(HEADER_ACCEPT_VERSION, "1.0")
            .addHeader(HEADER_API_META_DATA, "src=android,dest=castcle")
            .addHeader(HEADER_CONTENT_TYPE, "application/json")
            .addHeader(HEADER_DEVICE, Build.MODEL)
            .addHeader(HEADER_PLATFORM, "Android ${Build.VERSION.RELEASE}")
            .build()
    }

    private fun getAccessToken(): AccessTokenEntity {
        return runBlocking {
            database.accessToken().get().firstOrNull() ?: AccessTokenEntity()
        }
    }

    private fun Request.buildWithAccessToken(): Request.Builder {
        val accessToken = headers[HEADER_AUTHORIZATION] ?: getAccessToken().accessToken
        return if (accessToken.isBlank()) {
            newBuilder()
        } else {
            val accessTokenWithPrefix = if (accessToken.startsWith(AUTHORIZATION_PREFIX)) {
                accessToken
            } else {
                "$AUTHORIZATION_PREFIX$accessToken"
            }
            if (BuildConfig.DEBUG) {
                Timber.d("$HEADER_AUTHORIZATION($url) : $accessTokenWithPrefix")
            }
            newBuilder().addHeader(HEADER_AUTHORIZATION, accessTokenWithPrefix)
        }
    }

}