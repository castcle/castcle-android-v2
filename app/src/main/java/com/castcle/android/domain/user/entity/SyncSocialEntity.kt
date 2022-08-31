package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.user.type.SocialType

@Entity(tableName = TABLE_SYNC_SOCIAL)
data class SyncSocialEntity(
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_active") val active: Boolean = false,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_autoPost") val autoPost: Boolean = false,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_avatar") val avatar: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_displayName") val displayName: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_provider") val provider: SocialType = SocialType.Twitter,
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_socialId") val socialId: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_userId") val userId: String = "",
    @ColumnInfo(name = "${TABLE_SYNC_SOCIAL}_userName") val userName: String = "",
) {

    companion object {
        fun map(response: UserResponse?): List<SyncSocialEntity> {
            return listOfNotNull(
                response?.syncSocial?.facebook,
                response?.syncSocial?.google,
                response?.syncSocial?.twitter,
            ).map { map ->
                SyncSocialEntity(
                    active = map.active ?: false,
                    autoPost = map.autoPost ?: false,
                    avatar = map.avatar.orEmpty(),
                    displayName = map.displayName.orEmpty(),
                    id = map.id.orEmpty(),
                    provider = SocialType.getFromId(map.provider),
                    socialId = map.socialId.orEmpty(),
                    userId = response?.id.orEmpty(),
                    userName = map.userName.orEmpty(),
                )
            }
        }

        fun map(response: List<UserResponse>?): List<SyncSocialEntity> {
            return response.orEmpty().map { map(it) }.flatten()
        }
    }

}