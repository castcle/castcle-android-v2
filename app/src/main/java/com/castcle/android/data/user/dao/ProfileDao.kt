package com.castcle.android.data.user.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.ProfileWithResultEntity

@Dao
interface ProfileDao {

    @Query("DELETE FROM $TABLE_PROFILE")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ProfileEntity>)

    @Query("SELECT * FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, ProfileWithResultEntity>

    @Transaction
    suspend fun replace(sessionId: Long, items: List<ProfileEntity>) {
        delete(sessionId)
        insert(items)
    }

}