package com.castcle.android.data.core.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_LOAD_KEY
import com.castcle.android.domain.core.entity.LoadKeyEntity
import com.castcle.android.domain.core.type.LoadKeyType

@Dao
interface LoadKeyDao {

    @Query("DELETE FROM $TABLE_LOAD_KEY WHERE loadKey_loadType = :loadType")
    suspend fun delete(loadType: LoadKeyType)

    @Query("SELECT * FROM $TABLE_LOAD_KEY WHERE loadKey_sessionId = :sessionId")
    suspend fun get(sessionId: Long): List<LoadKeyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: LoadKeyEntity)

    @Transaction
    suspend fun replace(item: LoadKeyEntity) {
        delete(item.loadType)
        insert(item)
    }

}