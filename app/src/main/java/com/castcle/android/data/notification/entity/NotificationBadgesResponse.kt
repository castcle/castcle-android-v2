package com.castcle.android.data.notification.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class NotificationBadgesResponse(
    @SerializedName("page") val page: Int? = null,
    @SerializedName("profile") val profile: Int? = null,
    @SerializedName("system") val system: Int? = null,
)