package com.castcle.android.data.cast.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.domain.cast.entity.CastEntity

@Dao
interface CastDao {

    @Query("DELETE FROM $TABLE_CAST")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CastEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: CastEntity)

    @Query("UPDATE $TABLE_CAST SET casts_commentCount = casts_commentCount + 1 WHERE casts_id = :castId")
    suspend fun increaseCommentCount(castId: String)

}