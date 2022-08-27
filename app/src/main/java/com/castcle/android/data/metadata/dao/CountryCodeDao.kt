package com.castcle.android.data.metadata.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_COUNTRY_CODE
import com.castcle.android.domain.metadata.entity.CountryCodeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<CountryCodeEntity>)

    @Query("SELECT * FROM $TABLE_COUNTRY_CODE ORDER BY countryCode_name")
    fun retrieve(): Flow<List<CountryCodeEntity>>

}