package com.castcle.android.data.user.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SYNC_SOCIAL
import com.castcle.android.domain.user.entity.SyncSocialEntity

@Dao
interface SyncSocialDao {

    @Query("DELETE FROM $TABLE_SYNC_SOCIAL")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SyncSocialEntity>)

}