package com.castcle.android.domain.feed.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_FEED
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.feed.type.FeedType

@Entity(tableName = TABLE_FEED)
data class FeedEntity(
    @ColumnInfo(name = "${TABLE_FEED}_campaignMessage", defaultValue = "NULL")
    val campaignMessage: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_campaignName", defaultValue = "NULL")
    val campaignName: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_feedId", defaultValue = "")
    val feedId: String = "",
    @ColumnInfo(name = "${TABLE_FEED}_id", defaultValue = "0") @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_PROFILE}_ignoreReportContentId", defaultValue = "[]")
    val ignoreReportContentId: List<String> = listOf(),
    @ColumnInfo(name = "${TABLE_FEED}_originalCastId", defaultValue = "NULL")
    val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_originalUserId", defaultValue = "NULL")
    val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_referenceCastId", defaultValue = "NULL")
    val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_referenceUserId", defaultValue = "NULL")
    val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_type", defaultValue = "")
    val type: FeedType = FeedType.Content,
)