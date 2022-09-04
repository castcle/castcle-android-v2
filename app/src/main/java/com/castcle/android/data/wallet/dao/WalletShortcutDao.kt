package com.castcle.android.data.wallet.dao

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WALLET_SHORT_CUT
import com.castcle.android.domain.wallet.entity.WalletShortcutEntity
import com.castcle.android.domain.wallet.entity.WalletShortcutWithResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WalletShortcutDao {

    @Query("DELETE FROM $TABLE_WALLET_SHORT_CUT")
    suspend fun delete()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(items: List<WalletShortcutEntity>)

    @Query("SELECT * FROM $TABLE_WALLET_SHORT_CUT ORDER BY walletShortcut_order ASC")
    @Transaction
    fun retrieve(): Flow<List<WalletShortcutWithResultEntity>>

}