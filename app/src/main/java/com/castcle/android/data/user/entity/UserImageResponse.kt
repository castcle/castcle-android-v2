package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.*
import com.google.gson.annotations.SerializedName

@Keep
data class UserImageResponse(
    @SerializedName("avatar") val avatar: PhotoResponse? = null,
    @SerializedName("cover") val cover: PhotoResponse? = null,
)