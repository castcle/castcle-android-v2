package com.castcle.android.domain.authentication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
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

    fun isGuest() = type is AccessTokenType.Guest

    companion object {
        fun map(response: GetGuestAccessTokenResponse?) = AccessTokenEntity(
            accessToken = response?.accessToken ?: "",
            refreshToken = response?.refreshToken ?: "",
            type = AccessTokenType.Guest,
        )

        fun map(response: LoginResponse?) = AccessTokenEntity(
            accessToken = response?.accessToken ?: "",
            refreshToken = response?.refreshToken ?: "",
            type = AccessTokenType.Member,
        )
    }

}