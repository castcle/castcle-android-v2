package com.castcle.android.domain.core.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_LOAD_KEY
import com.castcle.android.domain.core.type.LoadKeyType

@Entity(tableName = TABLE_LOAD_KEY)
data class LoadKeyEntity(
    @ColumnInfo(name = "${TABLE_LOAD_KEY}_loadKey") val loadKey: String? = null,
    @ColumnInfo(name = "${TABLE_LOAD_KEY}_loadType") val loadType: LoadKeyType = LoadKeyType.Feed,
    @ColumnInfo(name = "${TABLE_LOAD_KEY}_sessionId") @PrimaryKey val sessionId: Long = 0L,
)