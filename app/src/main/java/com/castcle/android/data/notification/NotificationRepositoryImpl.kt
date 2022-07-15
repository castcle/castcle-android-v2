package com.castcle.android.data.notification

import com.castcle.android.core.api.NotificationApi
import com.castcle.android.core.extensions.apiCall
import com.castcle.android.core.storage.database.CastcleDatabase
import com.castcle.android.domain.notification.NotificationRepository
import com.castcle.android.domain.notification.entity.NotificationBadgesEntity
import org.koin.core.annotation.Factory

@Factory
class NotificationRepositoryImpl(
    private val api: NotificationApi,
    private val database: CastcleDatabase,
) : NotificationRepository {

    override suspend fun fetchNotificationsBadges() {
        val response = apiCall { api.getNotificationBadges() }
        database.notificationBadges().insert(NotificationBadgesEntity.map(response))
    }

}