package com.castcle.android.data.wallet.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_DASHBOARD
import com.castcle.android.domain.wallet.entity.WalletDashboardEntity
import com.castcle.android.domain.wallet.entity.WalletDashboardWithResultEntity
import com.castcle.android.domain.wallet.type.WalletDashboardType
import com.castcle.android.domain.wallet.type.WalletHistoryFilter
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletDashboardDao {

    @Query("DELETE FROM $TABLE_WALLET_DASHBOARD")
    suspend fun delete()

    @Query("DELETE FROM $TABLE_WALLET_DASHBOARD WHERE walletDashboard_type != :type")
    suspend fun deleteExcludeType(type: WalletDashboardType)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<WalletDashboardEntity>)

    @Query("SELECT * FROM $TABLE_WALLET_DASHBOARD ORDER BY walletDashboard_createAt DESC")
    @Transaction
    fun retrieve(): Flow<List<WalletDashboardWithResultEntity>>

    @Query("UPDATE $TABLE_WALLET_DASHBOARD SET walletDashboard_filter = :filter")
    suspend fun updateFilter(filter: WalletHistoryFilter)

}