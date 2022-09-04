package com.castcle.android.data.user.entity

import androidx.annotation.Keep
import com.castcle.android.core.base.response.*
import com.castcle.android.data.core.entity.CountResponse
import com.google.gson.annotations.SerializedName

@Keep
data class UserResponse(
    @SerializedName("aggregator") val aggregator: AggregatorResponse? = null,
    @SerializedName("avatar") val avatar: PhotoResponse? = null,
    @SerializedName("blocked") val blocked: Boolean? = null,
    @SerializedName("blocking") val blocking: Boolean? = null,
    @SerializedName("canUpdateCastcleId") val canUpdateCastcleId: Boolean? = null,
    @SerializedName("castcleId") val castcleId: String? = null,
    @SerializedName("casts") val casts: Int? = null,
    @SerializedName("contact") val contact: UserContactResponse? = null,
    @SerializedName("createdAt") val createdAt: String? = null,
    @SerializedName("displayName") val displayName: String? = null,
    @SerializedName("dob") val dob: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("followed") val followed: Boolean? = null,
    @SerializedName("followers") val followers: CountResponse? = null,
    @SerializedName("following") val following: CountResponse? = null,
    @SerializedName("id") val id: String? = null,
    @SerializedName("images") val images: UserImageResponse? = null,
    @SerializedName("links") val links: UserLinkResponse? = null,
    @SerializedName("linkSocial") val linkSocial: SocialProviderResponse? = null,
    @SerializedName("mobile") val mobile: UserMobileResponse? = null,
    @SerializedName("order") val order: Int? = null,
    @SerializedName("overview") val overview: String? = null,
    @SerializedName("passwordNotSet") val passwordNotSet: Boolean? = null,
    @SerializedName("pdpa") val pdpa: Boolean? = null,
    @SerializedName("syncSocial") val syncSocial: SocialProviderResponse? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("userId") val userId: String? = null,
    @SerializedName("verified") val verified: VerifiedResponse? = null,
)