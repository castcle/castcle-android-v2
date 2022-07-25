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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<SearchEntity>)

    @Query("SELECT * FROM $TABLE_SEARCH WHERE search_sessionId = :sessionId")
    @Transaction
    fun pagingSource(sessionId: Long): PagingSource<Int, SearchWithResultEntity>

}