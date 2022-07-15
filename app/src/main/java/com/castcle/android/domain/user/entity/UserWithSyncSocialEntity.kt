package com.castcle.android.domain.user.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.core.constants.TABLE_USER

data class UserWithSyncSocialEntity(
    @Relation(parentColumn = "${TABLE_USER}_id", entityColumn = "${TABLE_SYNC_SOCIAL}_userId")
    val syncSocial: List<SyncSocialEntity> = listOf(),
    @Embedded val user: UserEntity = UserEntity(),
)