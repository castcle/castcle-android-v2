package com.castcle.android.domain.notification

interface NotificationRepository {
    suspend fun fetchNotificationsBadges()
}