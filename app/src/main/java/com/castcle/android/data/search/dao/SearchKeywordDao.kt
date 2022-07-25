package com.castcle.android.data.search.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_SEARCH_KEYWORD
import com.castcle.android.domain.search.entity.SearchKeywordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchKeywordDao {

    @Query("DELETE FROM $TABLE_SEARCH_KEYWORD")
    suspend fun delete()

    @Query("SELECT * FROM $TABLE_SEARCH_KEYWORD WHERE searchKeyword_sessionId = :sessionId")
    suspend fun get(sessionId: Long): List<SearchKeywordEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SearchKeywordEntity)

    @Query("SELECT * FROM $TABLE_SEARCH_KEYWORD WHERE searchKeyword_sessionId = :sessionId")
    fun retrieve(sessionId: Long): Flow<List<SearchKeywordEntity>>

}