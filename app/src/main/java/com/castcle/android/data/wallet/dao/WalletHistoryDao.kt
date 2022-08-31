package com.castcle.android.data.wallet.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_HISTORY
import com.castcle.android.domain.wallet.entity.WalletHistoryEntity

@Dao
interface WalletHistoryDao {

    @Query("DELETE FROM $TABLE_WALLET_HISTORY")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<WalletHistoryEntity>)

}