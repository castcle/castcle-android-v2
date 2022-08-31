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