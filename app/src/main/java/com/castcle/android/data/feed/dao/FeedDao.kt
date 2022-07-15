package com.castcle.android.data.feed.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_FEED
import com.castcle.android.domain.feed.entity.FeedEntity
import com.castcle.android.domain.feed.entity.FeedWithResultEntity

@Dao
interface FeedDao {

    @Query("DELETE FROM $TABLE_FEED")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<FeedEntity>)

    @Query("SELECT * FROM $TABLE_FEED")
    @Transaction
    fun pagingSource(): PagingSource<Int, FeedWithResultEntity>

}