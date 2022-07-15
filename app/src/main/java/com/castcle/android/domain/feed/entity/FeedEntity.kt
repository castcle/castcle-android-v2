package com.castcle.android.domain.feed.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_FEED
import com.castcle.android.domain.feed.type.FeedType

@Entity(tableName = TABLE_FEED)
data class FeedEntity(
    @ColumnInfo(name = "${TABLE_FEED}_campaignMessage") val campaignMessage: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_campaignName") val campaignName: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_id") @PrimaryKey val id: String = "",
    @ColumnInfo(name = "${TABLE_FEED}_originalCastId") val originalCastId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_originalUserId") val originalUserId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_referenceCastId") val referenceCastId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_referenceUserId") val referenceUserId: String? = null,
    @ColumnInfo(name = "${TABLE_FEED}_type") val type: FeedType = FeedType.Content,
)