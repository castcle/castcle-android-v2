package com.castcle.android.data.cast.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_CAST
import com.castcle.android.domain.cast.entity.CastEntity
import com.castcle.android.domain.cast.entity.CastWithUserEntity
import com.castcle.android.domain.cast.type.CastType

@Dao
interface CastDao {

    @Query("UPDATE $TABLE_CAST SET casts_recastCount = casts_recastCount - 1 WHERE casts_id = :castId")
    suspend fun decreaseRecastCount(castId: String)

    @Query("DELETE FROM $TABLE_CAST")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_CAST WHERE casts_id = :castId")
    suspend fun delete(castId: String)

    @Query("SELECT * FROM $TABLE_CAST WHERE casts_id = :castId")
    suspend fun get(castId: String): CastWithUserEntity?

    @Query("SELECT * FROM $TABLE_CAST WHERE casts_referenceCastId = :referenceCastId AND casts_type = :type AND casts_authorId = :userId")
    suspend fun get(referenceCastId: String, type: CastType, userId: String): CastEntity?

    @Query("UPDATE $TABLE_CAST SET casts_commentCount = casts_commentCount + 1 WHERE casts_id = :castId")
    suspend fun increaseCommentCount(castId: String)

    @Query("UPDATE $TABLE_CAST SET casts_quoteCount = casts_quoteCount + 1 WHERE casts_id = :castId")
    suspend fun increaseQuoteCastCount(castId: String)

    @Query("UPDATE $TABLE_CAST SET casts_recastCount = casts_recastCount + 1 WHERE casts_id = :castId")
    suspend fun increaseRecastCount(castId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: CastEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CastEntity>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(item: CastEntity)

    @Query("UPDATE $TABLE_CAST SET casts_recasted = :recasted WHERE casts_id = :castId")
    suspend fun updateRecasted(castId: String, recasted: Boolean)

    @Query("UPDATE $TABLE_CAST SET casts_reported = :reported WHERE casts_id = :castId")
    suspend fun updateReported(castId: String, reported: Boolean)

}