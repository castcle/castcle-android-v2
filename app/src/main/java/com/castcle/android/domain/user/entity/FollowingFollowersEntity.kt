package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_FOLLOWING_FOLLOWERS

@Entity(tableName = TABLE_FOLLOWING_FOLLOWERS)
data class FollowingFollowersEntity(
    @ColumnInfo(name = "${TABLE_FOLLOWING_FOLLOWERS}_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "${TABLE_FOLLOWING_FOLLOWERS}_sessionId") val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_FOLLOWING_FOLLOWERS}_userId") val userId: String = "",
)