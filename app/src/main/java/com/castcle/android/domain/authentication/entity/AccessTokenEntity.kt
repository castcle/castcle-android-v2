package com.castcle.android.domain.authentication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.auth0.android.jwt.JWT
import com.castcle.android.core.constants.TABLE_ACCESS_TOKEN
import com.castcle.android.data.authentication.entity.GetGuestAccessTokenResponse
import com.castcle.android.data.authentication.entity.LoginResponse
import com.castcle.android.domain.authentication.type.AccessTokenType

@Entity(tableName = TABLE_ACCESS_TOKEN)
data class AccessTokenEntity(
    val accessToken: String = "",
    @PrimaryKey val id: Long = 0,
    val refreshToken: String = "",
    val type: AccessTokenType = AccessTokenType.Guest
) {

    fun getAccountId(): String {
        return try {
            JWT(accessToken).claims["id"]?.asString() ?: ""
        } catch (exception: Exception) {
            ""
        }
    }

    fun isGuest() = type is AccessTokenType.Guest

    companion object {
        fun map(response: GetGuestAccessTokenResponse?) = AccessTokenEntity(
            accessToken = response?.accessToken.orEmpty(),
            refreshToken = response?.refreshToken.orEmpty(),
            type = AccessTokenType.Guest,
        )

        fun map(response: LoginResponse?, type: AccessTokenType? = null) = AccessTokenEntity(
            accessToken = response?.accessToken.orEmpty(),
            refreshToken = response?.refreshToken.orEmpty(),
            type = type ?: AccessTokenType.Member,
        )
    }

}