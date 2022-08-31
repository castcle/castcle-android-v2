package com.castcle.android.data.user.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_FOLLOWING_FOLLOWERS
import com.castcle.android.domain.user.entity.FollowingFollowersEntity
import com.castcle.android.domain.user.entity.FollowingFollowersWithResultEntity

@Dao
interface FollowingFollowersDao {

    @Query("DELETE FROM $TABLE_FOLLOWING_FOLLOWERS")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_FOLLOWING_FOLLOWERS WHERE followingFollowers_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<FollowingFollowersEntity>)

    @Query("SELECT * FROM $TABLE_FOLLOWING_FOLLOWERS WHERE followingFollowers_sessionId = :sessionId")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, FollowingFollowersWithResultEntity>

}