package com.castcle.android.core.api

import com.castcle.android.data.notification.entity.NotificationBadgesResponse
import retrofit2.Response
import retrofit2.http.GET

interface NotificationApi {

    @GET("v2/notifications/badges")
    suspend fun getNotificationBadges(): Response<NotificationBadgesResponse>

}