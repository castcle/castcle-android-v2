package com.castcle.android.data.authentication.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_RECURSIVE_REFRESH_TOKEN
import com.castcle.android.domain.authentication.entity.RecursiveRefreshTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecursiveRefreshTokenDao {

    @Query("DELETE FROM $TABLE_RECURSIVE_REFRESH_TOKEN")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecursiveRefreshTokenEntity)

    @Query("SELECT * FROM $TABLE_RECURSIVE_REFRESH_TOKEN")
    fun retrieve(): Flow<List<RecursiveRefreshTokenEntity>>

}