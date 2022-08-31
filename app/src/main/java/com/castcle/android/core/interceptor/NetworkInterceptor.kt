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