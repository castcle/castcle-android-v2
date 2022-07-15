package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class GetFacebookUserProfileResponse(
    @SerializedName("email") val email: String? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("picture") val picture: Picture? = null,
) {

    data class Data(
        @SerializedName("url") val url: String? = null,
    )

    data class Picture(
        @SerializedName("data") val data: Data? = null,
    )

}