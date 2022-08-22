package com.castcle.android.data.search.dao

import androidx.paging.PagingSource
import androidx.room.*
import com.castcle.android.core.constants.TABLE_SEARCH
import com.castcle.android.domain.search.entity.SearchEntity
import com.castcle.android.domain.search.entity.SearchWithResultEntity

@Dao
interface SearchDao {

    @Query("DELETE FROM $TABLE_SEARCH")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_SEARCH WHERE search_sessionId = :sessionId")
    suspend fun delete(sessionId: Long)

    @Query("DELETE FROM $TABLE_SEARCH WHERE search_originalCastId = :castId")
    suspend fun deleteByOriginalCast(castId: String)

    @Query("DELETE FROM $TABLE_SEARCH WHERE search_referenceCastId = :castId")
    suspend fun deleteByReferenceCast(castId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SearchEntity>)

    @Query("SELECT * FROM $TABLE_SEARCH WHERE search_sessionId = :sessionId")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, SearchWithResultEntity>

    @Query("UPDATE $TABLE_SEARCH SET search_ignoreReportContentId = :ignoreReportContentId WHERE search_id = :id")
    suspend fun updateIgnoreReportContentId(id: String, ignoreReportContentId: List<String>)

}