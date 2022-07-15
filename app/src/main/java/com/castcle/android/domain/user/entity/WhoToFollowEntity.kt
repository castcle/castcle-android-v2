package com.castcle.android.domain.user.entity

import androidx.room.*
import com.castcle.android.core.constants.TABLE_WHO_TO_FOLLOW
import com.castcle.android.data.user.entity.UserResponse

@Entity(tableName = TABLE_WHO_TO_FOLLOW)
data class WhoToFollowEntity(
    @ColumnInfo(name = "${TABLE_WHO_TO_FOLLOW}_id") @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(name = "${TABLE_WHO_TO_FOLLOW}_userId") val userId: String = "",
) {

    companion object {
        fun map(response: List<UserResponse>?): List<WhoToFollowEntity> {
            return response.orEmpty().map {
                WhoToFollowEntity(userId = it.id ?: "")
            }
        }
    }

}