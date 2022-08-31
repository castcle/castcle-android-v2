package com.castcle.android.domain.search.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SEARCH_KEYWORD

@Entity(tableName = TABLE_SEARCH_KEYWORD)
data class SearchKeywordEntity(
    @ColumnInfo(name = "${TABLE_SEARCH_KEYWORD}_keyword") val keyword: String = "",
    @ColumnInfo(name = "${TABLE_SEARCH_KEYWORD}_sessionId") @PrimaryKey val sessionId: Long = System.currentTimeMillis(),
)