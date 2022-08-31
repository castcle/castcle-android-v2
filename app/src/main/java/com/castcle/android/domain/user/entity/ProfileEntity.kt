package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.user.type.ProfileType

@Entity(tableName = TABLE_PROFILE)
data class ProfileEntity(
    @ColumnInfo(name = "${TABLE_PROFILE}_createdAt", defaultValue = "0")
    val createdAt: Long = 0L,
    @ColumnInfo(name = "${TABLE_PROFILE}_id", defaultValue = "0") @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "${TABLE_PROFILE}_ignoreReportContentId", defaultValue = "[]")
    val ignoreReportContentId: List<String> = listOf(),
    @ColumnInfo(name = "${TABLE_PROFILE}_originalCastId", defaultValue = "NULL")
    val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_originalUserId", defaultValue = "NULL")
    val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_referenceCastId", defaultValue = "NULL")
    val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_referenceUserId", defaultValue = "NULL")
    val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_PROFILE}_sessionId", defaultValue = "0")
    val sessionId: Long = 0L,
    @ColumnInfo(name = "${TABLE_PROFILE}_type", defaultValue = "")
    val type: ProfileType = ProfileType.Content,
)