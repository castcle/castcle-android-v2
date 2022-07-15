package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.user.type.ProfileType

@Entity(tableName = TABLE_PROFILE)
data class ProfileEntity(
    @ColumnInfo(name = "${TABLE_PROFILE}_id") @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "${TABLE_PROFILE}_originalCastId") val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_originalUserId") val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_referenceCastId") val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_referenceUserId") val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_sessionId") val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_PROFILE}_type") val type: ProfileType = ProfileType.Content
)