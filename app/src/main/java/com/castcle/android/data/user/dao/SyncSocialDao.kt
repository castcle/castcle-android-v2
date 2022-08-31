package com.castcle.android.data.user.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.domain.user.entity.SyncSocialEntity

@Dao
interface SyncSocialDao {

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL WHERE syncSocial_userId IN (:userId)")
    suspend fun delete(userId: List<String>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SyncSocialEntity>)

}