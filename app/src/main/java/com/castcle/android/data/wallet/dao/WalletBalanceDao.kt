package com.castcle.android.data.wallet.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_BALANCE
import com.castcle.android.domain.wallet.entity.WalletBalanceEntity

@Dao
interface WalletBalanceDao {

    @Query("DELETE FROM $TABLE_WALLET_BALANCE")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WalletBalanceEntity)

}