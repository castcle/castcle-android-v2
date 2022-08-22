package com.castcle.android.data.user.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_PROFILE
import com.castcle.android.domain.user.entity.ProfileEntity
import com.castcle.android.domain.user.entity.ProfileWithResultEntity
import com.castcle.android.domain.user.type.ProfileType

@Dao
interface ProfileDao {

    @Query("DELETE FROM $TABLE_PROFILE")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_originalCastId = :castId")
    suspend fun deleteByOriginalCast(castId: String)

    @Query("DELETE FROM $TABLE_PROFILE WHERE profile_referenceCastId = :castId")
    suspend fun deleteByReferenceCast(castId: String)

    @Query("SELECT DISTINCT profile_sessionId FROM $TABLE_PROFILE WHERE profile_originalUserId = :userId AND profile_type = :type")
    suspend fun getExistSessionIdByUserId(userId: String, type: ProfileType): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<ProfileEntity>)

    @Query("SELECT * FROM $TABLE_PROFILE WHERE profile_sessionId = :sessionId ORDER BY profile_createdAt DESC")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, ProfileWithResultEntity>

    @Transaction
    suspend fun replace(sessionId: Long, items: List<ProfileEntity>) {
        delete(sessionId)
        insert(items)
    }

    @Query("UPDATE $TABLE_PROFILE SET profile_ignoreReportContentId = :ignoreReportContentId WHERE profile_id = :id")
    suspend fun updateIgnoreReportContentId(id: String, ignoreReportContentId: List<String>)

}