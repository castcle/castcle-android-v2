package com.castcle.android.data.user.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_LINK_SOCIAL
import com.castcle.android.domain.user.entity.LinkSocialEntity

@Dao
interface LinkSocialDao {

    @Query("DELETE FROM $TABLE_LINK_SOCIAL")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<LinkSocialEntity>)

}