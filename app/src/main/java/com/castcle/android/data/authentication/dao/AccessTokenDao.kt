package com.castcle.android.data.authentication.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_ACCESS_TOKEN
import com.castcle.android.domain.authentication.entity.AccessTokenEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AccessTokenDao {

    @Query("SELECT * FROM $TABLE_ACCESS_TOKEN")
    suspend fun get(): AccessTokenEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: AccessTokenEntity)

    @Query("SELECT * FROM $TABLE_ACCESS_TOKEN")
    fun retrieve(): Flow<AccessTokenEntity?>

}