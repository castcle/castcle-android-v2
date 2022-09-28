package com.castcle.android.presentation.sign_up.update_profile.entity

import com.castcle.android.data.user.entity.UserLinkResponse
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class UploadImageRequest(
    var currentCastcleId: String? = null,

    @SerializedName("images")
    var images: UpLoadImagesRequest? = null,
)

data class UserUpdateRequest(
    var currentCastcleId: String? = null,

    @SerializedName("castcleId")
    var castcleIdEdit: String? = null,

    @SerializedName("displayName")
    var displayName: String? = null,

    @SerializedName("overview")
    var overview: String? = "",

    @SerializedName("dob")
    var dob: String? = null,

    @SerializedName("contact")
    var contract: Contract? = null,

    @SerializedName("links")
    var links: UserLinkResponse? = null,

    @SerializedName("images")
    var images: UpLoadImagesRequest? = null
)

data class Contract(
    @SerializedName("countryCode")
    var countryCode: String? = null,

    @SerializedName("phone")
    var phone: String? = null,

    @SerializedName("email")
    var email: String? = null,
)

data class UpLoadImagesRequest(
    @SerializedName("avatar")
    var avatar: String? = null,

    @SerializedName("cover")
    var cover: String? = null,

    var castcleId: String? = null
)

fun UpLoadImagesRequest.toStringImageRequest(): String {
    return Gson().toJson(this)
}

fun String.toImageRequestModel(): UpLoadImagesRequest {
    return Gson().fromJson(this, UpLoadImagesRequest::class.java)
}

enum class PhotoSelectedState {
    AVATAR_SELECT,
    COVER_SELECT,
    NON
}