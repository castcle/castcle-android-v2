package com.castcle.android.domain.search.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SEARCH

@Entity(tableName = TABLE_SEARCH)
data class SearchEntity(
    @ColumnInfo(name = "${TABLE_SEARCH}_id") @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_SEARCH}_originalCastId") val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_originalUserId") val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_referenceCastId") val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_referenceUserId") val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_SEARCH}_sessionId") val sessionId: Long = 0L,
)