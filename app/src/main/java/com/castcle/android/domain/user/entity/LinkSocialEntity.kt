package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_LINK_SOCIAL
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.data.user.entity.UserResponse
import com.castcle.android.domain.user.type.SocialType

//@Entity(tableName = TABLE_LINK_SOCIAL)
//data class LinkSocialEntity(
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_active") val active: Boolean = false,
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_autoPost") val autoPost: Boolean = false,
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_avatar") val avatar: String = "",
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_displayName") val displayName: String = "",
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_id") @PrimaryKey val id: String = "",
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_provider") val provider: SocialType = SocialType.Twitter,
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_socialId") val socialId: String = "",
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_userId") val userId: String = "",
//    @ColumnInfo(name = "${TABLE_LINK_SOCIAL}_userName") val userName: String = "",
//)