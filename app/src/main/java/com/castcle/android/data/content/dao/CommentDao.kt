package com.castcle.android.data.content.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_COMMENT
import com.castcle.android.domain.content.entity.CommentEntity

@Dao
interface CommentDao {

    @Query("DELETE FROM $TABLE_COMMENT")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CommentEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: CommentEntity)

}