package com.castcle.android.domain.authentication.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.castcle.android.core.constants.TABLE_RECURSIVE_REFRESH_TOKEN

@Entity(tableName = TABLE_RECURSIVE_REFRESH_TOKEN)
data class RecursiveRefreshTokenEntity(@PrimaryKey val id: Long = 0)