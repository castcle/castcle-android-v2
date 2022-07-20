package com.castcle.android.data.search.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_RECENT_SEARCH
import com.castcle.android.domain.search.entity.RecentSearchEntity

@Dao
interface RecentSearchDao {

    @Query("DELETE FROM $TABLE_RECENT_SEARCH")
    suspend fun delete()

    @Query("SELECT * FROM $TABLE_RECENT_SEARCH ORDER BY recentSearch_timestamp DESC LIMIT 20")
    suspend fun get(): List<RecentSearchEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RecentSearchEntity)

}