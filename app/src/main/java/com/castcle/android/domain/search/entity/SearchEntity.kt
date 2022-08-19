package com.castcle.android.domain.search.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.core.constants.TABLE_SEARCH

@Entity(tableName = TABLE_SEARCH)
data class SearchEntity(
    @ColumnInfo(name = "${TABLE_SEARCH}_id", defaultValue = "0") @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_PROFILE}_ignoreReportContentId", defaultValue = "[]")
    val ignoreReportContentId: List<String> = listOf(),
    @ColumnInfo(name = "${TABLE_SEARCH}_originalCastId", defaultValue = "NULL")
    val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_originalUserId", defaultValue = "NULL")
    val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_referenceCastId", defaultValue = "NULL")
    val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_referenceUserId", defaultValue = "NULL")
    val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_sessionId", defaultValue = "0")
    val sessionId: Long = 0L,
)