package com.castcle.android.domain.search.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_RECENT_SEARCH

@Entity(tableName = TABLE_RECENT_SEARCH)
data class RecentSearchEntity(
    @ColumnInfo(name = "${TABLE_RECENT_SEARCH}_keyword") @PrimaryKey val keyword: String = "",
    @ColumnInfo(name = "${TABLE_RECENT_SEARCH}_timestamp") val timestamp: Long = System.currentTimeMillis(),
)