package com.castcle.android.data.notification.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_NOTIFICATION_BADGES
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationBadgesDao {

    @Query("DELETE FROM $TABLE_NOTIFICATION_BADGES")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: NotificationBadgesEntity)

    @Query("SELECT * FROM $TABLE_NOTIFICATION_BADGES")
    fun retrieve(): Flow<List<NotificationBadgesEntity>>

}