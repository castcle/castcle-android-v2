/* Copyright (c) 2021, Castcle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Castcle, 22 Phet Kasem 47/2 Alley, Bang Khae, Bangkok,
 * Thailand 10160, or visit www.castcle.com if you need additional information
 * or have any questions.
 *
 * Created by Prakan Sornbootnark on 15/08/2022. */

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