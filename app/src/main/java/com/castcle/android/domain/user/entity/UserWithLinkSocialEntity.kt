package com.castcle.android.domain.user.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_LINK_SOCIAL
import com.castcle.android.core.constants.TABLE_USER

data class UserWithLinkSocialEntity(
    @Relation(parentColumn = "${TABLE_USER}_id", entityColumn = "${TABLE_LINK_SOCIAL}_userId")
    val linkSocial: List<LinkSocialEntity> = listOf(),
    @Embedded val user: UserEntity = UserEntity(),
)