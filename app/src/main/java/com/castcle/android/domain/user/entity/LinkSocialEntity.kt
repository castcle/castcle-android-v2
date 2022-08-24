package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_LINK_SOCIAL
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.user.type.SocialType

@Entity(tableName = TABLE_LINK_SOCIAL)
data class LinkSocialEntity(
    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_provider", defaultValue = "")
    @PrimaryKey
    val provider: SocialType = SocialType.Twitter,
    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_socialId", defaultValue = "")
    val socialId: String = "",
    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_userId", defaultValue = "")
    val userId: String = "",
) {

    companion object {
        fun map(response: UserResponse?): List<LinkSocialEntity> {
            return listOfNotNull(
                response?.linkSocial?.facebook?.let { it to SocialType.Facebook },
                response?.linkSocial?.google?.let { it to SocialType.Google },
                response?.linkSocial?.twitter?.let { it to SocialType.Twitter },
            ).map { (map, provider) ->
                LinkSocialEntity(
                    provider = provider,
                    socialId = map.socialId.orEmpty(),
                    userId = response?.id.orEmpty(),
                )
            }
        }
    }

}