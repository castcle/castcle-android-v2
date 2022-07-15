package com.castcle.android.domain.notification.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.castcle.android.core.constants.TABLE_NOTIFICATION_BADGES
import com.castcle.android.data.notification.entity.NotificationBadgesResponse

@Entity(tableName = TABLE_NOTIFICATION_BADGES)
data class NotificationBadgesEntity(
    @PrimaryKey val id: Long = 0,
    val page: Int = 0,
    val profile: Int = 0,
    val system: Int = 0,
) {

    fun total() = page.plus(profile).plus(system)

    companion object {
        fun map(response: NotificationBadgesResponse?) = NotificationBadgesEntity(
            page = response?.page ?: 0,
            profile = response?.profile ?: 0,
            system = response?.system ?: 0,
        )
    }

}