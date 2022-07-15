package com.castcle.android.data.user.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_WHO_TO_FOLLOW
import com.castcle.android.domain.user.entity.WhoToFollowEntity
import com.castcle.android.domain.user.entity.WhoToFollowWithResultEntity

@Dao
interface WhoToFollowDao {

    @Query("DELETE FROM $TABLE_WHO_TO_FOLLOW")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<WhoToFollowEntity>)

    @Query("SELECT * FROM $TABLE_WHO_TO_FOLLOW")
    @Transaction
    fun pagingSource(): PagingSource<Int, WhoToFollowWithResultEntity>

}